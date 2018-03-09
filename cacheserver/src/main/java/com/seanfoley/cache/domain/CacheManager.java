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
import com.seanfoley.cache.interfaces.ICacheManager;
import com.seanfoley.cache.interfaces.ICacheStorage;
import com.seanfoley.cache.interfaces.ICacheValue;


/**
 *
 * This class implements the ICacheManager interface and is intended
 * to be used as the top-level object by dependent clients.  It
 * provides the basic methods to add/get/remove items to/from the cache.
 *
 */
public class CacheManager implements ICacheManager {

    // Let's decouple the actual storage implementation.
    // This way we can swap it out for something different
    // and not impact our client. In the future this
    // would be pluggable via a config file which would
    // allow different types of storage such as disk based.
    private final ICacheStorage _storage;

    private static CacheManager _instance = null;

    public static ICacheManager Instance(int capacity ) {

        // If we were using the concept of a namespace/cache
        // association we would want to create a cache-per-namespace.
        // But for our current implementation, we only have a global
        // cache, so it's important that we only ever return one
        // instance for the host process regardless of the thread-context.
        synchronized (CacheManager.class) {
            if (null == _instance) {
                _instance = new CacheManager(capacity);
            }
        }

        return _instance;
    }

    /**
     * CacheManager C-tor used to specify the capacity of
     * the cache.
     * @param itemCapacityCount the number of items/elements that the cache
     *                          will store before the Least Recently Used (LRU)
     *                          algorithm kicks in.
     */
    private CacheManager( int itemCapacityCount ) {

        _storage = new LinkedHashMapCacheStorage( itemCapacityCount );
    }

    /**
     * Adds a key/value pair to the cache. If the key already
     * exists, the value is replaced with this one. If the
     * key does not exist, the key/value pair is added to the cache.
     * @param key The key to use for the cache item
     * @param value The value of the item to be cached
     * @return true if the key/value was added (or updated)
     */
    @Override
    public boolean add(ICacheKey key, ICacheValue value) {
        return _storage.add(key, value);
    }

    /**
     * Gets the value from the cache based on the key.
     * @param key The key to use for the cache lookup
     * @return An ICacheValue object if found; otherwise null.
     */
    @Override
    public ICacheValue get(ICacheKey key) {
        return _storage.get(key);
    }

    /**
     * Removes the key (and value) from the cache
     * @param key The key to remove from the cache
     * @return true if the key was found and removed.
     */
    @Override
    public boolean remove(ICacheKey key) {
        return _storage.remove(key);
    }
}
