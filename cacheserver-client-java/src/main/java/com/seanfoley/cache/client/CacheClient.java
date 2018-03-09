/*
 *  Copyright (C) 2018 Sean Foley  All Rights Reserved.
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted.  Enjoy.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.seanfoley.cache.client;

import java.util.List;
import java.util.Base64;

/**
 * This is the proxy class used by a client to
 * "talk" to the cacheserver.  This class handles all
 * of the protocol formatting/parsing and network
 * transport activities.
 */
public class CacheClient {

    // The protocol uses +OK or -ERR to indicate
    // success or failure.
    private final String CRLF = "\r\n";

    private enum ServerResult { Ok, Err }

    private final List<CacheServer> _servers;

    // todo - we probably want to make this pluggable
    private IKeySpaceSelector _keySpaceSelector =
            new ModuloKeySpaceSelector();

    private static CacheClient _instance;

    /**
     * Use this method to create a CacheClient object.
     * @param cacheServers the list of cacheservers the client
     *                     should shard the keyspace across. There
     *                     must be at least 1 server in the list.
     * @return CacheClient object instance
     */
    public static CacheClient Create( List<CacheServer> cacheServers ){

        if( null == cacheServers ) {
            throw new IllegalArgumentException(
                    "The cacheServers list is null. There must be at least 1 server."
            );
        }

        // todo - might not need to be configured as a singleton.
        // The resources (socket) follow a create/use/destroy pattern
        // so there isn't really a risk of getting into some whacky
        // state if we allow multiple object creation. The only shared
        // object state is the server list, which if that mutated
        // between object instantiations, that would *be bad.*
        // I guess this might be best as a singleton
        synchronized (CacheClient.class) {
            if (null == _instance) {
                _instance = new CacheClient(cacheServers);
            }
        }

        return _instance;
    }

    /**
     * Adds the specified key/value pair to the cache
     * @param key the cache key
     * @param value the value to store in the cache
     * @return true if the key/value pair is added to the cache
     */
    public boolean add( String key, String value ){

        // We will use a string builder because the value data
        // could be very bulky.
        StringBuilder builder = new StringBuilder();

        // +add key value\r\n
        builder.append("+add ");
        builder.append(key);
        builder.append(" ");

        // Encoding the string will allow us to handle
        // spaces, ctrl characters, and embedded
        // carriage returns/newlines
        builder.append(base64Encode(value));

        builder.append("\r\n");

        String request = builder.toString();

        String response = rawSend(key, request);

        return parseServerStatusResponse( response ) == ServerResult.Ok;
    }

    /**
     * Gets a key/value pair from the cache
     * @param key the cache key to search for
     * @return the value if found, null if the key isn't found
     */
    public String get(String key){

        String request = String.format("+get %s%s", key, CRLF);

        String response = rawSend(key, request);

        return parseDataResponse(response );
    }

    /**
     * Removes the key/value pair from the cache
     * @param key the key to use to find the key/value pair to remove
     * @return true if the key was found and removed, false otherwise
     */
    public boolean remove(String key){

        String request = String.format("+remove %s%s", key, CRLF);

        String response = rawSend(key, request);

        return parseServerStatusResponse( response ) == ServerResult.Ok;
    }


    /**
     * Helper method to encode a string.
     * @param source the source string
     * @return a base64 encoded string
     */
    public static String base64Encode( String source ){

        // todo - might want to create a Encoder/Decoder Interface
        byte[] encoded = Base64.getEncoder().encode(source.getBytes() );

        return new String(encoded);
    }

    /**
     * Helper method to decode a based64 encoded string
     * @param source the based64 encoded string
     * @return a decoded string
     */
    public static String base64Decode( String source ) {

        // todo - might want to create a Encoder/Decoder Interface
        byte[] decoded = Base64.getDecoder().decode( source );

        return new String(decoded);
    }

    /**
     * Orchestration menthod that shards the key to a server, then
     * sends the request.
     * @param key the cache key
     * @param request the request to send to the server
     * @return the server response
     */
    private String rawSend( String key, String request ){

        int offset = (int)_keySpaceSelector.shard(key, _servers.size() );

        ICacheServerTransport transport = new CacheServerTcpTransport();

        transport.setCacheServer(_servers.get(offset));

        transport.connect();

        return transport.rawSend(request);
    }

    /**
     * No direct construction.
     * @param servers The cache server list is the collection
     *                of cache servers that can be used for
     *                caching key/values
     */
    private CacheClient( List<CacheServer> servers ){
        _servers = servers;
    }

    private String parseDataResponse( String response ){

        //
        // todo - needs more error handling/checking
        //
        String[] s = response.split(" ");

        String value = null;

        if( s.length > 0 ) {
            // todo we should add a lot more bounds checking
            switch (parseServerStatusResponse(s[0])) {
                case Ok:
                    value = base64Decode(s[1]);
            }

        }
        return value;
    }

    private ServerResult parseServerStatusResponse( String response )
    {
        final String OK = "+OK";
        final String ERROR = "-ERR";

        boolean result = response.contains(OK);
        if( result ){ return ServerResult.Ok; }

        result = response.contains(ERROR);
        if( result ){ return ServerResult.Err; }

        return ServerResult.Err;
    }
}
