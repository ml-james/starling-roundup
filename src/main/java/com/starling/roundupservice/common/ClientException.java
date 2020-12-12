package com.starling.roundupservice.common;

import java.util.HashMap;
import java.util.Map;

public class ClientException extends RuntimeException
{

    private final Map<String, String> error;

    public ClientException(String source, String errorMessage)
    {
        super();
        this.error = new HashMap<>()
        {{
            put(source, errorMessage);
        }};
    }

    public Map<String, String> getError()
    {
        return this.error;
    }
}
