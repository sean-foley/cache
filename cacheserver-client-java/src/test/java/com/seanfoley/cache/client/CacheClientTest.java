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

import mockit.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CacheClientTest {

    private @Mocked CacheServerTcpTransport transport;


    @Test
    public void add() {

        new Expectations() {{

            // mock out the network socket call
            // and simulate the server responding
            // with a successful response;
            transport.rawSend(anyString);
            result = "+OK\r\n";
        }};

        String key = "key";
        String value = "value";

        List<CacheServer> servers = new ArrayList<>();
        CacheServer server = new CacheServer("localhost", 5000);
        servers.add( server );

        CacheClient client = CacheClient.Create(servers);

        boolean result = client.add(key, value);

        assertTrue(result);
    }

    @Test
    public void addFail() {

        new Expectations() {{

            // mock out the network socket call
            // and simulate the server responding
            // with a failed response
            transport.rawSend(anyString);
            result = "-ERR bad key\r\n";
        }};

        String key = "bad with space";
        String value = "value";

        List<CacheServer> servers = new ArrayList<>();
        CacheServer server = new CacheServer("localhost", 5000);
        servers.add( server );

        CacheClient client = CacheClient.Create(servers);

        boolean result = client.add(key, value);

        assertFalse(result);
    }

    @Test
    public void get() {

        String value = "mockedvalue";

        String encodedValue = CacheClient.base64Encode(value);

        new Expectations() {{

            // mock out the network socket call
            // and simulate the server responding
            // with a successful response;
            transport.rawSend(anyString);

            // Note no CRLF ending because the real
            // CacheServerTcpTransport method we are mocking
            // would strip the CRLF out of a response string
            result = String.format("+OK %s", encodedValue);
        }};

        String key = "key";

        List<CacheServer> servers = new ArrayList<>();
        CacheServer server = new CacheServer("localhost", 5000);
        servers.add( server );

        CacheClient client = CacheClient.Create(servers);

        String result = client.get(key);

        assertEquals(value, result);
    }

    @Test
    public void getFail() {

        new Expectations() {{

            // mock out the network socket call
            // and simulate the server responding
            // with a failed response;
            transport.rawSend(anyString);

            result = "+ERR key not found\r\n";
        }};

        List<CacheServer> servers = new ArrayList<>();
        CacheServer server = new CacheServer("localhost", 5000);
        servers.add( server );

        CacheClient client = CacheClient.Create(servers);

        // Simulate a bad key
        String key = "keynotincache";

        String result = client.get(key);

        assertNull(result);
    }

    @Test
    public void remove() {

        new Expectations() {{

            // mock out the network socket call
            // and simulate the server responding
            // with a successful response;
            transport.rawSend(anyString);

            result = "+OK\r\n";
        }};

        String key = "key";

        List<CacheServer> servers = new ArrayList<>();
        CacheServer server = new CacheServer("localhost", 5000);
        servers.add( server );

        CacheClient client = CacheClient.Create(servers);

        boolean result = client.remove(key);

        assertTrue(result);
    }

    @Test
    public void removeFail() {

        new Expectations() {{

            // mock out the network socket call
            // and simulate the server responding
            // with a failed response;
            transport.rawSend(anyString);

            result = "-ERR\r\n";
        }};


        List<CacheServer> servers = new ArrayList<>();
        CacheServer server = new CacheServer("localhost", 5000);
        servers.add( server );

        CacheClient client = CacheClient.Create(servers);

        // this doesn't matter
        String key = "key";

        // We've mocked the transport to simulate
        // the server responding with an -ERR to the
        // request
        boolean result = client.remove(key);

        // Call should result in a fail
        assertFalse(result);
    }

    @Test
    public void encodeDecode() {

        String source = "Some string with spaces and CRLF\r\n";

        String encoded = CacheClient.base64Encode(source);

        // Make sure no sneaky nulls
        assertNotNull(encoded);

        // Can't be the same
        assertNotEquals(encoded, source);

        String decoded = CacheClient.base64Decode(encoded);

        // Make sure no sneaky nulls
        assertNotNull(decoded);

        assertEquals(decoded, source);

    }
}