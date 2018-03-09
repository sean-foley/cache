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

import java.net.*;
import java.io.*;

/**
 * This class implements the cache server protocol using
 * TCP as the transport.  It uses a sync (blocking) socket
 * as the underlying connection and follows an HTTP 1.0 like
 * connection lifetime: open a socket, send the request,
 * read the response, then close the socket.
 *
 * From an implementation standpoint, this class is very dumbed down
 * to only handle sending/receiving string blobs over a socket. The
 * higher level protocol formatting/parsing the response is outside
 * of the responsibility of this class.
 *
 * Usage Example:
 * 1. call setCacheServer() to set the server (name and port) to talk to
 * 2. call connect() to establish a connection
 * 3. call rawSend() to send the request and capture the response return value
 */
public class CacheServerTcpTransport implements ICacheServerTransport {

    private CacheServer _server;

    @Override
    public void setCacheServer(CacheServer server) {

        if( null == server ) {
            throw new NullPointerException("The server is null, which is not allowed.");
        }

        _server = server;
    }

    @Override
    public boolean connect() {

        // Stubbed
        return true;
    }

    /**
     * This method opens a connection, sends the data, then
     * closes the connection (like simple HTTP 1.0 connection
     * handling).  Future improvements would be to add keep-alive
     * support, async support, etc.
     * @param request a string blob
     * @return the response from the server
     */
    @Override
    public String rawSend(String request) {

        DataOutputStream output = null;
        DataInputStream  input  = null;
        Socket socket = null;

        String response = null;

        try
        {
            socket = new Socket( _server.getName(), _server.getPort() );

            // todo this should be externalized in a config file
            final int TIMEOUT_MS = 10000;

            // The socket read operation will timeout
            // after this amount of time and throw a
            // SocketTimeoutException. The socket will
            // still be valid.
            socket.setSoTimeout(TIMEOUT_MS);

            output = new DataOutputStream( socket.getOutputStream() );
            input  = new DataInputStream( socket.getInputStream() );

            /*
            The server is going to respond with a greeting so
            let's read that to get it out of the way
            todo using deprecated readLine need to refactor
            */
            response = input.readLine();

            // The server protocol handling right now isn't
            // that sophisticated, so byte dump to optimize
            // the wire transfer
            output.write(request.getBytes());

            // todo using deprecated readLine need to refactor
            response = input.readLine();

        }
        catch(IOException exception){

            System.err.printf("IOException: %s", exception.getMessage() );
        }
        finally {

            // Clean up everything
            try {
                if (null != output) {
                    output.close();
                }
                if (null != input) {
                    input.close();
                }
                if( null != socket ) {
                    socket.close();
                }
            }
            catch(Exception exception)
            {
                System.err.printf("Exception trying to close socket resources: %s", exception.getMessage() );
            }
        }

        return response;
    }
}
