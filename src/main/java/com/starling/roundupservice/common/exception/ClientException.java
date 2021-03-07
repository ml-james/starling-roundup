package com.starling.roundupservice.common.exception;

import java.util.HashMap;
import java.util.Map;

public class ClientException extends RuntimeException
{

    private final Map<String, String> error;

    public ClientException(String source, String errorMessage)
    {
        super(errorMessage);
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
