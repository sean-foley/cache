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

package com.seanfoley.cache.domain;

import com.seanfoley.cache.interfaces.ICacheKey;
import com.seanfoley.cache.interfaces.ICacheValue;

/**
 * This class implements the server protocol that clients
 * can use to interact with the cache remotely. The protocol
 * follows a request/response pattern and is extremely simple:
 *
 * <command><space><key><space>|<value><terminator>
 *
 * command: a string prefixed with a +
 * key: a string representing the cache key. it cannot contain spaces
 * value: a string representing the value. it cannot contain any spaces
 *        or non-printable characters, including CRLF. Please implement
 *        some sort of escaping/encoding to overcome this limitation.
 *        For instance, based64 encoding the string will solve this problem
 *        at the expense of encoding/decoding overhead and a bigger
 *        value object.
 * terminator: A carriage-return/line-feed (\r\n) is used to terminate the command.
 *
 * Example Commands:
 * +add key value\r\n
 * +get key\r\n
 * +remove key\r\n
 *
 * The server responds with:
 * <status><space>|<value>|<message><terminator>
 * status: +OK or -ERR
 * value: The cache value (only if the command returns a value)
 * <message> a message, usually related to an error.
 * terminator: A carriage-return/line-feed (\r\n) is used to terminate the response.
 *
 * Example Responses:
 * +OK\r\n
 * +OK some-cache-value\r\n
 * -ERR key not found\r\n
 *
 */
class RequestParser {

    private static final String COMMAND_ADD = "+add";
    private static final String COMMAND_GET = "+get";
    private static final String COMMAND_REMOVE = "+remove";

    public static final int TOKEN_ADD = 0;
    public static final int TOKEN_GET = 1;
    public static final int TOKEN_REMOVE = 2;
    public static final int TOKEN_UNKNOWN = -1;

    private static final String[] COMMANDS = {
            COMMAND_ADD,
            COMMAND_GET,
            COMMAND_REMOVE
    };

    private static final int[] TOKENS = {
            TOKEN_ADD,
            TOKEN_GET,
            TOKEN_REMOVE,
            TOKEN_UNKNOWN
    };

    private CacheKey _key = null;
    private CacheValue _value = null;

    // Default is we don't know what type of command (token)
    // was given.
    private int _token = TOKEN_UNKNOWN;

    public int getToken() { return _token; }
    public ICacheKey getCacheKey() { return _key; }
    public ICacheValue getCacheValue() { return _value; }

    public void parse(byte[] bytes) {

        String buffer = new String( bytes );

        String[] s = buffer.split(" ");

        if( s.length > 0 )
        {
            _token = tokenize(s[ 0 ] );
        }


        final String CRLF = "\r\n";
        final String EMPTY = "";

        switch( _token )
        {
            case TOKEN_ADD:
                if( s.length == 3 ) {
                    _key   = new CacheKey(s[1]);
                    _value = new CacheValue(s[2].replace(CRLF, EMPTY));
                }

                break;

            case TOKEN_GET:
                if( s.length == 2 )
                {
                    _key   = new CacheKey(s[1].replace(CRLF, EMPTY));
                }

                break;

            case TOKEN_REMOVE:
                if( s.length == 2 )
                {
                    _key   = new CacheKey(s[1].replace(CRLF, EMPTY));
                }
                break;

            case TOKEN_UNKNOWN:
            default:
        }

    }

    private int tokenize( String s ) {

        // The tokenizer isn't super efficient. If we had a
        // big knarly string of 10KB that wasn't formatted how we
        // expect it to be, the tokenizer would search
        // through all of that. Since we know the maximum length
        // of a command, we can truncate the string and only
        // search a portion.
        final int MAX_CMD_LENGTH = 10;
        int offset = s.length() >= MAX_CMD_LENGTH ? MAX_CMD_LENGTH: s.length();

        String cmd = s.substring(0, offset );

        for( int i = 0; i < COMMANDS.length; i++ )
        {
            if( cmd.contains(COMMANDS[i] ) ) {
                return TOKENS[i];
            }
        }

        return TOKEN_UNKNOWN;
    }

}
