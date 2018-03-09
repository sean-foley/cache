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

package com.seanfoley.cache.interfaces;

/**
 * The ICacheStorage interface is used internally to
 * provide the cache storage.  The intent is for the
 * ICacheManager client implementation to use an
 * ICacheStorage for the concrete cache storage
 * implementation (i.e. a bridge pattern)
 */
public interface ICacheStorage {

    /**
     * Adds the key/value to the cache
     * @param key The cache key
     * @param value The cache value
     * @return True if the item was successfully added.
     */
    boolean add(ICacheKey key, ICacheValue value);

    /**
     * Gets the value from the cache based on the key
     * @param key The cache key
     * @return an ICacheValue object if the key is found
     * in the cache, or null if not found
     */
    ICacheValue get( ICacheKey key );

    /**
     * Removes the key (and value) from the cache
     * @param key The cache key to remove
     * @return True if successful
     */
    boolean remove( ICacheKey key );

    /**
     * The count of items (not bytes) in the cache
     * @return The number of items in the cache
     */
    int count();

}
