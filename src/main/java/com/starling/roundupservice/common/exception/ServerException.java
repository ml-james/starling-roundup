package com.starling.roundupservice.common.exception;

import java.util.HashMap;
import java.util.Map;

public class ServerException extends RuntimeException {

  private final Map<String, String> error;

  public ServerException(String source, String errorMessage) {
    super();
    this.error = new HashMap<>() {{ put(source, errorMessage); }};
  }

  public Map<String, String> getError() {
    return error;
  }
}
