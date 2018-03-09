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
import com.seanfoley.cache.interfaces.ICacheValue;
import com.seanfoley.cache.interfaces.ICacheManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The TcpSocketServerAsync class provides network-level socket
 * support. This allows clients to interact with the cache server
 * via TCP.  The protocol is a simple text line terminated with a \r\n.
 * This allows a simple telnet client to interact with the cache server.
 *
 * Examples:
 *
 * telnet localhost 5000
 * +add key somevalue\r\n
 * +get key\r\n
 * +remove key\r\n
 *
 */
public class TcpSocketServerAsync
{
    // todo externalize the port to a config file
    private final int PORT = 5000;

    public TcpSocketServerAsync()
    {
        try
        {
            // Create an AsynchronousServerSocketChannel that will listen on a port
            final AsynchronousServerSocketChannel listener =
                    AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(PORT));

            // Start the socket and listen for connections
            listener.accept( null, new CompletionHandler<AsynchronousSocketChannel,Void>() {

                @Override
                public void completed(AsynchronousSocketChannel channel, Void att)
                {
                    // Accept the next connection
                    listener.accept( null, this );

                    System.out.println( "New inbound client connection accepted" );

                    // Greet the client
                    channel.write( ByteBuffer.wrap( "Cache Server - let's store some stuff\r\n".getBytes() ) );

                    // todo - the buffer should be externally configurable/tunable
                    final int BUFFER_SIZE = 4096;

                    // todo - the timeout value of the socket if there is no activity
                    final int TIMEOUT_SECONDS = 20;

                    // todo and hack/hack - Because we only have only one buffer
                    // and are not accumulating bytes until we see the termination
                    // we can only accept up to BUFFER_SIZE amount of data to cache.
                    ByteBuffer byteBuffer = ByteBuffer.allocate( BUFFER_SIZE );

                    try
                    {
                        // Read the first line
                        int bytesRead = channel.read( byteBuffer ).get( TIMEOUT_SECONDS, TimeUnit.SECONDS );

                        boolean running = true;
                        while( bytesRead != -1 && running )
                        {
                            if( byteBuffer.position() >= 0 )
                            {
                                // Make the buffer ready to read
                                byteBuffer.flip();

                                // Convert the buffer into a line
                                byte[] request = new byte[bytesRead];
                                byteBuffer.get(request, 0, bytesRead);

                                // Do we have terminating characters?
                                boolean terminated = request[bytesRead - 2] == '\r';
                                terminated &= request[bytesRead - 1] == '\n';

                                if (terminated) {
                                    String response = handleRequest(request);

                                    channel.write(ByteBuffer.wrap(response.getBytes()));
                                }
                            }

                            // Clear to get ready for the next round of data
                            byteBuffer.clear();

                            // Read more data
                            bytesRead = channel.read( byteBuffer ).get( TIMEOUT_SECONDS, TimeUnit.SECONDS );
                        }

                    }
                    catch (InterruptedException | ExecutionException exception)
                    {
                        exception.printStackTrace();
                    }
                    catch (TimeoutException e)
                    {
                        System.out.println( "Connection timed out, closing connection" );
                    }

                    channel.write( ByteBuffer.wrap( "Good Bye\n".getBytes() ) );

                    System.out.println( "Closing client connection" );
                    try
                    {
                        // Close the connection if we need to
                        if( channel.isOpen() )
                        {
                            channel.close();
                        }
                    }
                    catch (IOException exception)
                    {
                        exception.printStackTrace();
                    }
                }

                @Override
                public void failed(Throwable exc, Void att) {
                    ///...
                }
            });
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * The method will parse the raw byte buffer into a request,
     * execute the requested operation, and build response for the
     * client.
     * @param request the raw byte buffer received over the socket
     * @return the response to the request
     */
    private String handleRequest(byte[] request)
    {
        RequestParser parser = new RequestParser();
        parser.parse(request);

        int token = parser.getToken();

        // todo the number of items needs to be refactored to a config file
        final int ITEMS = 100000;

        ICacheManager cacheManager = CacheManager.Instance(ITEMS);

        ICacheKey key = null;
        ICacheValue value = null;

        String response;

        switch( token )
        {
            case RequestParser.TOKEN_ADD:

                key = parser.getCacheKey();
                value = parser.getCacheValue();

                boolean ok = cacheManager.add(key, value );

                if(ok) {
                    response = new ResponseFormatter(
                            ResponseFormatter.ServerResult.Ok).format();
                }
                else {
                    response = new ResponseFormatter(
                            ResponseFormatter.ServerResult.Err,
                            "error adding key to cache").format();
                }

                break;

            case RequestParser.TOKEN_GET:
                key = parser.getCacheKey();
                value = cacheManager.get(key);

                if( null != value) {

                    response = new ResponseFormatter(
                            ResponseFormatter.ServerResult.Ok, value ).format();

                }
                else {
                    response = new ResponseFormatter(
                            ResponseFormatter.ServerResult.Err,
                            "key not found").format();
                }

                break;

            case RequestParser.TOKEN_REMOVE:
                key = parser.getCacheKey();

                if( cacheManager.remove(key) ) {
                    response = new ResponseFormatter(
                            ResponseFormatter.ServerResult.Ok).format();
                }
                else {
                    response = new ResponseFormatter(
                            ResponseFormatter.ServerResult.Err,
                            "removing key not found").format();
                }
                break;

            case RequestParser.TOKEN_UNKNOWN:
            default:

                response = new ResponseFormatter(
                        ResponseFormatter.ServerResult.Err,
                        "unknown or unexpected command").format();
        }

        return response;
    }
}