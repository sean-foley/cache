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

import java.util.UUID;

public class ModuloKeySpaceSelectorTest {

    @Test
    public void shard() {

        IKeySpaceSelector selector = new ModuloKeySpaceSelector();

        int node = selector.shard("Some Key Doesn't Matter", 1 );

        // This should always select the first node (0)
        assertEquals(node, 0);
    }

    @Test(expected = IllegalArgumentException.class )
    public void keyValidation() {

        IKeySpaceSelector selector = new ModuloKeySpaceSelector();

        String nullKeysAreEvil = null;

        int node = selector.shard(nullKeysAreEvil, 1);
    }

    @Test(expected = IllegalArgumentException.class )
    public void nodeValidationNegative() {

        IKeySpaceSelector selector = new ModuloKeySpaceSelector();

        String key = "key";
        long negativeNodesAreNoBueno = -1;

        int node = selector.shard(key, negativeNodesAreNoBueno);
    }

    @Test(expected = IllegalArgumentException.class )
    public void nodeValidationZero() {

        IKeySpaceSelector selector = new ModuloKeySpaceSelector();

        String key = "key";
        long zeroNodes = 0;

        int node = selector.shard(key, zeroNodes);
    }

    @Test
    public void shardDistribution(){

        int MAX_NODES = 10;
        int MAX_KEYS = 1000;

        long[] bucketTally = new long[MAX_NODES];

        for( int i = 0; i < MAX_KEYS; i++ ) {

            UUID uid = UUID.randomUUID();

            String key = uid.toString();

            IKeySpaceSelector selector = new ModuloKeySpaceSelector();

            int node = selector.shard(key, MAX_NODES );

            // Track how often the hashing has
            // selected this bucket.
            bucketTally[node] +=1;
        }

        for( int j = 0; j < MAX_NODES; j++ )
        {
            // Let's check the distribution to make
            // sure we at least selected each bucket
            assertTrue(bucketTally[j] != 0 );
        }
    }
}