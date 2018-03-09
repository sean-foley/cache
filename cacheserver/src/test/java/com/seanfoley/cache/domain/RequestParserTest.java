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

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestParserTest {

    @Test
    public void commandUnknown() {
        String payload = "+whattypeofcommandisthis key\r\n";

        RequestParser parser = new RequestParser();

        parser.parse( payload.getBytes() );

        assertTrue(RequestParser.TOKEN_UNKNOWN == parser.getToken() );

        // Because we don't know what we're dealing with, we
        // shouldn't try to extract anything else that was sent
        // (i.e. key, value, etc.)
        assertNull(parser.getCacheKey());
    }

    @Test
    public void commandNoLineTerminator() {
        String payload = "+whattypeofcommandisthis key";

        RequestParser parser = new RequestParser();

        parser.parse( payload.getBytes() );

        assertTrue(RequestParser.TOKEN_UNKNOWN == parser.getToken() );

        // Because we don't know what we're dealing with, we
        // shouldn't try to extract anything else that was sent
        // (i.e. key, value, etc.)
        assertNull(parser.getCacheKey());
    }

    @Test
    public void commandSuperShortPayload() {
        String payload = "a";

        RequestParser parser = new RequestParser();

        parser.parse( payload.getBytes() );

        assertTrue(RequestParser.TOKEN_UNKNOWN == parser.getToken() );

        // Because we don't know what we're dealing with, we
        // shouldn't try to extract anything else that was sent
        // (i.e. key, value, etc.)
        assertNull(parser.getCacheKey());
    }

    @Test
    public void commandRemove() {

        String payload = "+remove key\r\n";

        RequestParser parser = new RequestParser();

        parser.parse( payload.getBytes() );

        assertTrue(RequestParser.TOKEN_REMOVE == parser.getToken() );

        assertEquals( "key", parser.getCacheKey().getKey());
    }

    @Test
    public void commandGet() {

        String payload = "+get key\r\n";

        RequestParser parser = new RequestParser();

        parser.parse( payload.getBytes() );

        assertTrue(RequestParser.TOKEN_GET == parser.getToken() );

        assertEquals( "key", parser.getCacheKey().getKey());

        // Note - there won't be a value because that gets *returned* and the
        // CommandParser is only parsing what is sent from a client

    }

    @Test
    public void commandAdd() {

        String payload = "+add key value\r\n";

        RequestParser parser = new RequestParser();

        parser.parse( payload.getBytes() );

        assertTrue(RequestParser.TOKEN_ADD == parser.getToken() );

        assertEquals( "key", parser.getCacheKey().getKey());

        assertEquals( "value", parser.getCacheValue().getValue());
    }
}