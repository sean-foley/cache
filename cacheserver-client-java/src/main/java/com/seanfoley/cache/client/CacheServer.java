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

/**
 * This is a simple container class for the
 * properties required to describe a cache server.
 *
 */
public class CacheServer {

    private String _name;
    private int _port;

    /**
     * Constructor
     * @param name the name or ip address of the cache server
     * @param port the network port
     */
    public CacheServer( String name, int port ) {
        _name = validateName( name );
        _port = validatePort(port );
    }

    //
    // Accessors/mutators follow
    //
    public void setName( String value ) { _name = validateName(value); }
    public String getName() { return _name; }

    public void setPort( int value ) { _port = validatePort(value); }
    public int getPort() { return _port; }

    //
    // validators to enforce correctness
    //

    private String validateName( String name ) {

        if( null == name ) {
            throw new IllegalArgumentException(
                    "The cache server name is null, which is not valid."
            );
        }

        final int EMPTY = 0;

        if( EMPTY == name.length() ) {
            throw new IllegalArgumentException(
                    "The cache server name is empty, which is not valid."
            );
        }

        return name;
    }

    private int validatePort( int port ) {

        final int MIN = 0;
        final int MAX = 65535;

        if( MIN > port ) {
            throw new IllegalArgumentException(
                    "The port is negative, which is not a valid network port"
            );
        }

        if( MAX < port ) {
            throw new IllegalArgumentException(
                    "The port exceeds the maximum network port"
            );
        }

        return port;
    }
}
