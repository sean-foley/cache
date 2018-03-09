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

public class CacheValueTest {

    @Test
    public void setValue() {
        Object value = "string value";

        CacheValue cacheValue = new CacheValue( value );

        assertEquals(value, cacheValue.getValue());
    }

    @Test
    public void getValue() {

        Object value = "string value";

        CacheValue cacheValue = new CacheValue( value );

        assertEquals(value, cacheValue.getValue());
    }

    @Test
    public void constructors() {

        // The object has helper c-tors for string/object/int
        // So we're gonna test them, which will also make sure if
        // the signature is changed (i.e a c-tor is removed) it
        // should cause this test to fail
        {
            int value = 99;

            CacheValue cacheValue = new CacheValue(value);

            assertEquals( value, (int) cacheValue.getValue() );
        }
        {
            String value = "99";

            CacheValue cacheValue = new CacheValue(value);

            // We are assuming if the object types are the same, the
            // underlying values are the same
            assertEquals( value, cacheValue.getValue());
        }
        {
            byte[] value = {98, 99};

            CacheValue cacheValue = new CacheValue(value);

            assertEquals(value.getClass(), cacheValue.getValue().getClass() );
        }

    }
}