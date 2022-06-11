package com.ndr.socialasteroids.presentation.payload.response;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static Map<String, Object> createResponseBody(Pair<?,?>... pairs) throws ClassCastException
    {
        Map<String, Object> responseBody = new HashMap<String, Object>();

        for (Pair<?,?> pair : pairs)
        {
            responseBody.put((String) pair.getKey(), pair.getValue());
        }
        
        return responseBody;
    }
    
}
