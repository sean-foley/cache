package com.seanfoley.cache.domain;

import com.seanfoley.cache.interfaces.ICacheValue;

/**
 * This class implements the server protocol that clients
 * can use to interact with the cache remotely. The protocol
 * follows a request/response pattern.This class implements
 * the response protocol, which is extremely simple:
 *
 * The server responds with:
 * <status><space>|<value>|<message><terminator>
 * status: +OK or -ERR
 * value: The cache value (only if the command returns a value)
 * <message> a message, usually related to an error.
 * terminator: A carriage-return/line-feed (\r\n) is used to terminate the response.
 *
 * Example Responses:
 * +OK\r\n
 * +OK some-cache-value\r\n
 * -ERR key not found\r\n
 *
 */
public class ResponseFormatter {

    public enum ServerResult { Ok, Err }

    private ServerResult _serverResult;
    private String _errorMessage = null;
    private ICacheValue _cacheValue = null;

    ResponseFormatter(ServerResult result){ _serverResult = result; }
    ResponseFormatter(ServerResult result, ICacheValue value ) {

        // Let's enforce some correctness
        if( ServerResult.Ok == result ) {

            _serverResult = result;
            _cacheValue = value;
        }
        else {
            throw new IllegalArgumentException("a cache value can only be set when the server result is ok");
        }
    }

    ResponseFormatter(ServerResult result, String errorMessage){

        if( ServerResult.Err == result) {
            _serverResult = result;
            _errorMessage = errorMessage;
        }
        else {
            throw new IllegalArgumentException("the server result is ok, but you are trying to set an error message");
        }
    }


    //
    // Accessors/mutators
    //
    public void setServerResult(ServerResult value){ _serverResult = value; }
    public ServerResult getServerResult(){ return _serverResult; }

    public void setErrorMessage(String value){ _errorMessage = value; }
    public String getErrorMessage(){ return _errorMessage; }

    public void setCacheValue(ICacheValue value){ _cacheValue = value; }
    public ICacheValue getCacheValue(){ return _cacheValue; }

    /**
     * This method will use the object's state to format a
     * response to a client's request.
     * @return the formatted response that can be sent to the client
     */
    public String format() {

        final String CRLF = "\r\n";

        String response = null;

        switch( _serverResult )
        {
            case Ok:
                if( null == _cacheValue ) {
                    // Send a response that doesn't include
                    // a payload
                    response = String.format("+OK%s", CRLF);
                }
                else {
                    // We have a cache value payload, so
                    // add that to the response
                    response = String.format("+OK %s%s", _cacheValue.getValue().toString(), CRLF);
                }

                break;

            case Err:

                if( null == _errorMessage ){
                    response = String.format("-ERR%s", CRLF);
                }
                else {
                    // Include the optional error message
                    response = String.format("-ERR %s%s", _errorMessage, CRLF);
                }

                break;

            default:
                // Bad mojo... indicates someone probably exteneded the enum but
                // neglected to add a handler
                throw new IllegalArgumentException("An unknown SeverResult was encountered");
        }

        return response;
    }

}
