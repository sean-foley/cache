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

import java.util.zip.CRC32;

/**
 * This class implements a very simple consistent hash algorithm
 * to map a key to a particular node from N number of nodes.
 * The algorithm is how a simple memcached client selects
 * a memcached server to store the key/value: CRC32(key) modulo NODES
 */
public class ModuloKeySpaceSelector implements IKeySpaceSelector {

    /**
     * Sharding conceptually is distributing some key in a keyspace shared by
     * N number of nodes (servers).
     * @param key The key that should be evaluated
     * @param nodes The number of nodes that could possible store the key
     * @return 0<=value<nodes that indicates which node should store the key
     */
    @Override
    public int shard(String key, long nodes) {

        if( nodes <= 0 ) {
            throw new IllegalArgumentException(
                    "The nodes value must be a positive number"
            );
        }

        if( null == key )
        {
            throw new IllegalArgumentException(
                    "The key cannot be null."
            );
        }

        CRC32 crc32 = new CRC32();

        crc32.update(key.getBytes() );

        long hash = crc32.getValue();

        // A crc really doesn't care about the sign
        // but java doesn't do unsigned int primitive
        // types.  So we're abs'ing this shizzle
        return (int) Math.abs( hash % nodes );
    }
}
