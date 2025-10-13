package exceptions;

public class HttpRequestLineLengthException extends RuntimeException {
  public HttpRequestLineLengthException(int length) {
    super(String.format("HTTP request line consists of three parts, while have got: %d", length));
  }
}
