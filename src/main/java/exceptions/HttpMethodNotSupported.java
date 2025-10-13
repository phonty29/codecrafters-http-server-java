package exceptions;

import enums.HttpVersion;

public class HttpMethodNotSupported extends RuntimeException {

  public HttpMethodNotSupported(HttpVersion version, String method) {
    super(String.format("HTTP version %s does not support method: %s", version, method));
  }
}
