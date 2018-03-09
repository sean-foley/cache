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

package com.seanfoley.cache.client.example;

import com.seanfoley.cache.client.CacheServer;
import com.seanfoley.cache.client.CacheClient;

import java.util.List;
import java.util.ArrayList;


class Main {

    public static void main(String[] args) {

        try
        {
            System.out.println( "cacheserver java client example");

            // Our keys will be sharded across this server list.
            // If you only have 1 server, it will have the entire keyspace
            List<CacheServer>  servers = new ArrayList<>();

            // Change the server and port to what's
            // appropriate for your environment
            final String SERVER_NAME = "localhost";
            final int SERVER_PORT = 5000;

            servers.add(new CacheServer(SERVER_NAME, SERVER_PORT));

            // The client handles all the protocol interfacing
            // and server sharding
            CacheClient client = CacheClient.Create( servers );

            String key = "key";
            String value = "super-duper-awesome-value";

            System.out.printf( "Attempting to add key: %s, value: %s to  the cache...", key, value);
            boolean result = client.add(key,value );

            if( result ) {

                System.out.println("success!");

                System.out.printf( "Getting value from cache for key: %s...", key);
                String retrievedValue = client.get(key);
                if( null != retrievedValue){
                    System.out.printf("value: %s\r\n", retrievedValue);
                }
                else{
                    System.out.println( "key not found.");
                }

                System.out.printf( "removing key: %s\r\n", key );

                if( client.remove(key) ) {

                    String noValue = client.get(key);

                    if( null == noValue )
                    {
                        System.out.println("confirmed key is removed.");
                    }
                }

            }
            else
            {
                System.out.println("failed! :(");
            }
        }
        catch( Exception exception ){
            System.out.printf("Exception: %s\r\n", exception.getMessage() );
        }
    }
}