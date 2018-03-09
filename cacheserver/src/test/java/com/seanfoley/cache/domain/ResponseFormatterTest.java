package com.seanfoley.cache.domain;

import com.seanfoley.cache.interfaces.ICacheValue;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseFormatterTest {

    @Test
    public void getAndSetServerResult() {
        ResponseFormatter.ServerResult result = ResponseFormatter.ServerResult.Ok;

        // Set it to a different value than what we are going to use for the setter/getter
        ResponseFormatter response = new ResponseFormatter(ResponseFormatter.ServerResult.Err);

        response.setServerResult(result);

        assertTrue( response.getServerResult() == result );
    }

    @Test
    public void getAndSetMessage() {

        ResponseFormatter.ServerResult result = ResponseFormatter.ServerResult.Err;

        // Set it to a different value than what we are going to use for the setter/getter
        ResponseFormatter response = new ResponseFormatter(result,"dont care");

        String message = "Some message";

        response.setErrorMessage(message);

        assertEquals(response.getErrorMessage(), message );
    }

    @Test
    public void getAndSetCacheValue() {

        ResponseFormatter.ServerResult result = ResponseFormatter.ServerResult.Ok;

        // Set it to a different value than what we are going to use for the setter/getter
        ResponseFormatter response = new ResponseFormatter(result,new CacheValue("junk"));

        ICacheValue cacheValue = new CacheValue("some value");

        response.setCacheValue(cacheValue);

        assertEquals(response.getCacheValue().getValue(), cacheValue.getValue() );
    }

    @Test
    public void formatOkNoCacheValue() {

        ResponseFormatter.ServerResult result = ResponseFormatter.ServerResult.Ok;

        ResponseFormatter response = new ResponseFormatter(result);

        assertEquals(response.format(), "+OK\r\n" );
    }

    @Test
    public void formatOkWithCacheValue() {

        ResponseFormatter.ServerResult result = ResponseFormatter.ServerResult.Ok;

        ICacheValue cacheValue = new CacheValue("some value");

        ResponseFormatter response = new ResponseFormatter(result, cacheValue);

        String expected = String.format("+OK %s\r\n", cacheValue.getValue().toString());

        assertEquals(response.format(), expected);
    }

    @Test
    public void formatErrNoErrorMessage() {

        ResponseFormatter.ServerResult result = ResponseFormatter.ServerResult.Err;

        ResponseFormatter response = new ResponseFormatter(result);

        assertEquals(response.format(), "-ERR\r\n");
    }

    @Test
    public void formatErrWithErrorMessage() {

        String errorMessage = "Errors suck";
        ResponseFormatter.ServerResult result = ResponseFormatter.ServerResult.Err;

        ResponseFormatter response = new ResponseFormatter(result, errorMessage);

        String expected = String.format("-ERR %s\r\n", errorMessage);

        assertEquals(response.format(), expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorErrWithValue() {

        ResponseFormatter.ServerResult result = ResponseFormatter.ServerResult.Err;

        // Should throw an exception because how can you have an error
        // but successfully get a value?
        ResponseFormatter response = new ResponseFormatter(result, new CacheValue("blah"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorOkWithErrorMessage() {

        ResponseFormatter.ServerResult result = ResponseFormatter.ServerResult.Ok;

        // Should throw an exception because how can you have an error
        // but successfully get a value?
        ResponseFormatter response = new ResponseFormatter(result,"error message");
    }
}