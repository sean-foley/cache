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

import com.seanfoley.cache.interfaces.ICacheStorage;
import com.seanfoley.cache.interfaces.ICacheKey;
import com.seanfoley.cache.interfaces.ICacheValue;

import org.junit.Test;

import static org.junit.Assert.*;

public class LinkedHashMapCacheStorageTest {

    private final int ITEM_CAPACITY = 10;

    /*
    private ICacheStorage _storage = null;

    @Before
    public void beforeEachTest() {
        _storage = new LinkedHashMapCacheStorage(ITEM_CAPACITY);
    }

    @After
    public void afterEachTest() {
        // Nuke
        _storage = null;
    }
    */

    @Test
    public void add() {

        ICacheStorage storage = new LinkedHashMapCacheStorage(ITEM_CAPACITY);

        ICacheKey key = new CacheKey("key");
        ICacheValue value = new CacheValue("value");

        // We shouldn't have anything in cache
        assertTrue(storage.count() == 0 );

        boolean ok = storage.add(key, value);

        // We should have 1 item in the cache.
        assertTrue( storage.count() == 1 );
    }

    @Test
    public void get() {

        ICacheStorage storage = new LinkedHashMapCacheStorage(ITEM_CAPACITY);

        ICacheKey key = new CacheKey("key");
        ICacheValue value = new CacheValue("value");

        // We shouldn't have anything in cache
        assertTrue( storage.count() == 0 );

        boolean ok = storage.add(key, value);

        // Test the return value - should
        // be successful if we didn't throw
        // any exceptions
        assertTrue(ok);

        // We should have 1 item in the cache.
        assertTrue( storage.count() == 1 );

        Object retrieved = storage.get( key ).getValue();

        assertEquals( value.getValue(), retrieved);
    }

    @Test
    public void remove() {
        ICacheStorage storage = new LinkedHashMapCacheStorage(ITEM_CAPACITY);

        ICacheKey key = new CacheKey("key");
        ICacheValue value = new CacheValue("value");

        // We shouldn't have anything in cache
        assertTrue( storage.count() == 0 );

        storage.add(key, value);

        // We should have 1 item in the cache.
        assertTrue( storage.count() == 1 );

        storage.remove(key);

        // We shouldn't have anything in cache
        assertTrue( storage.count() == 0 );
    }

    @Test
    public void count() {

        ICacheStorage storage = new LinkedHashMapCacheStorage(ITEM_CAPACITY);

        // We shouldn't have anything in cache
        assertTrue( storage.count() == 0 );

        for( int i = 0; i < ITEM_CAPACITY; i++ ) {

            ICacheKey key = new CacheKey("key:" + i);
            ICacheValue value = new CacheValue("value:" + i);

            storage.add(key, value);
            assertTrue( storage.count() == i + 1);
        }
    }

    @Test
    public void lruEviction() {

        ICacheStorage storage = new LinkedHashMapCacheStorage(ITEM_CAPACITY);

        // We shouldn't have anything in cache
        assertTrue( storage.count() == 0 );

        for( int i = 0; i < ITEM_CAPACITY; i++ ) {

            ICacheKey key = new CacheKey(Integer.toString(i));
            ICacheValue value = new CacheValue(Integer.toString(i));

            storage.add(key, value);
            assertTrue( storage.count() == i + 1);
        }

        // We access from the last entered to the first entered.
        // Since the storage is tracking which item was touched last
        // this will make the largest item the "oldest" which
        // should get evicted if we add another item.
        for( int x = ITEM_CAPACITY - 1; x >= 0; x--) {

            CacheKey key = new CacheKey( Integer.toString(x));

            ICacheValue val = storage.get( key );
            assertEquals(val.getValue(), Integer.toString(x) );
        }

        // This should cause an eviction
        ICacheKey kickedYouToTheCurbKey = new CacheKey("byebye");
        ICacheValue kickedYouToTheCurbValue = new CacheValue("cya!");
        storage.add( kickedYouToTheCurbKey, kickedYouToTheCurbValue);

        // If we evicted, then we should only have up to our
        // item capacity. If we are over this, then we are not
        // properly enforcing capacity limits
        assertTrue( storage.count() == ITEM_CAPACITY );

        // We "touched" key 9 the as the first get() in the 2nd loop
        // and key 0 last (i.e. from 9 to 0), so 9 should be the LRU
        // key and it should be evicted.
        ICacheValue missing = storage.get(new CacheKey("9"));
        assertNull(missing);

        // Finally, for completeness, the key we used to cause
        // the eviction should be in the cache
        ICacheValue val = storage.get(kickedYouToTheCurbKey);
        assertEquals(kickedYouToTheCurbValue.getValue(), val.getValue() );
    }
}