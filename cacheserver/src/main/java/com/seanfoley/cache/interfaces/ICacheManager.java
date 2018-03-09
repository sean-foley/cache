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
 * The ICacheManager interface is intended to be called
 * by the client that is utilizing the cache. This should
 * be the client interface (API) to utilize the caching
 * functionality.
 */
public interface ICacheManager {

    /**
     * Adds the key/value pair to the cache.
     * @param key The key to use for the cache item
     * @param value The value of the item to be cached
     * @return true if the item was successfully added to
     * the cache.
     */
    boolean add(ICacheKey key, ICacheValue value );

    /**
     * Gets a value from the cache based on the cache key.
     * @param key The key to use for the cache lookup
     * @return An ICacheValue object if found, otherwise null.
     */
    ICacheValue get( ICacheKey key );

    /**
     * Removes the key (and value) from the cache
     * @param key The key to remove from the cache
     * @return True if the item was removed successfully
     */
    boolean remove( ICacheKey key );
}
