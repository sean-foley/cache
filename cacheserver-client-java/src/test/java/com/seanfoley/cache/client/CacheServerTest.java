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

import org.junit.Test;

import static org.junit.Assert.*;

public class CacheServerTest {

    private final int SERVER_PORT = 5000;
    private final String SERVER_NAME = "localhost";

    @Test
    public void setGetName() {

        CacheServer server = new CacheServer(SERVER_NAME, SERVER_PORT);

        String name = "something";

        server.setName(name);

        assertEquals(name, server.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setBadNameNull() {

        CacheServer server = new CacheServer(SERVER_NAME, SERVER_PORT);

        String name = null;

        // Should throw an exception
        server.setName(name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setBadNameEmpty() {

        CacheServer server = new CacheServer(SERVER_NAME, SERVER_PORT);

        String name = "";

        // Should throw an exception
        server.setName(name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badNameNullConstructor() {

        String name = null;

        CacheServer server = new CacheServer(name, SERVER_PORT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badNameEmptyConstructor() {

        String name = "";

        CacheServer server = new CacheServer(name, SERVER_PORT);
    }

    @Test
    public void getSetPort() {

        CacheServer server = new CacheServer(SERVER_NAME, SERVER_PORT);

        // must be a valid port of 0..65535
        int port = 5555;

        // this will overwrite what was set in the c-tor
        server.setPort(port);

        assertEquals(port, server.getPort());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setBadPortMinBeyondBounds() {

        CacheServer server = new CacheServer(SERVER_NAME, SERVER_PORT);

        // Choose a invalid port number outside of the 0..65535 range
        int port = -1;

        // Should throw an exception
        server.setPort(port);
    }

    @Test
    public void setBadPortMinBounds() {

        CacheServer server = new CacheServer(SERVER_NAME, SERVER_PORT);

        // Choose the min port number on the boundary of 0..65535
        int port = 0;

        // Should be ok
        server.setPort(port);

        // reading back to be complete
        assertEquals(port, server.getPort());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setBadPortMaxBeyondBounds() {

        CacheServer server = new CacheServer(SERVER_NAME, SERVER_PORT);

        // Choose a invalid port number outside of the 0..65535 range
        int port = 999999;

        // Should throw an exception
        server.setPort(port);
    }

    @Test
    public void setBadPortMaxBounds() {

        CacheServer server = new CacheServer(SERVER_NAME, SERVER_PORT);

        // Choose a port number on the max boundary 0..65535 range
        int port = 65535;

        // Should be ok
        server.setPort(port);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badPortConstructor() {

        // Choose the min port number on the boundary of 0..65535
        int port = -1;

        // Should throw an exception
        CacheServer server = new CacheServer(SERVER_NAME, port);
    }
}