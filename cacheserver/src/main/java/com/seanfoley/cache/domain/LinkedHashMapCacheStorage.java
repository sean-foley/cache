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
import com.seanfoley.cache.interfaces.ICacheStorage;
import com.seanfoley.cache.interfaces.ICacheValue;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class:
 * LinkedHashMapCacheStorage
 *
 * Description:
 * This class implements the ICacheStorage interface and uses a
 * LinkedHashMap as the underlying backing (data) store.  The
 * current implementation of this class provides for a fixed
 * maximum capacity of items (not bytes).  If item(s) are added
 * past the max capacity, the least-recently used item is evicted
 * and replaced with the new item.
 *
 */
public class LinkedHashMapCacheStorage implements ICacheStorage {

    private final LinkedHashMap<Object, ICacheValue> _map;

    private final int CAPACITY;

    LinkedHashMapCacheStorage( int capacity ) {

        CAPACITY = capacity;

        final float LOAD_FACTOR = 0.80f;
        final boolean ACCESS_ORDER = true;

        _map = new LinkedHashMap<Object, ICacheValue>(capacity, LOAD_FACTOR, ACCESS_ORDER) {
            @Override
            protected boolean removeEldestEntry(Map.Entry entry) {

                // If we are at capacity, then let's use a simple
                // eviction policy of removing the last item
                return size() > CAPACITY;
            }
        };
    }

    @Override
    public boolean add(ICacheKey key, ICacheValue value) {

        // todo - needs proper thread synchronization
         _map.put(key.getKey(), value );

        // The initial put() of a key could return
        // a null value. So the real way to confirm
        // if we are successful would be to test
        // the existence of the key after the put.
        // We are going to assume as long as there isn't
        // an exception, then the put() was successful.
         return true;
    }

    @Override
    public ICacheValue get(ICacheKey key) {

        // todo - needs proper thread synchronization
        return _map.get( key.getKey() );
    }

    @Override
    public boolean remove(ICacheKey key) {

        // todo - needs proper thread synchronization
        return _map.remove( key.getKey() ) != null;
    }

    @Override
    public int count() {
        // todo - needs proper thread synchronization
        return _map.size();
    }
}
