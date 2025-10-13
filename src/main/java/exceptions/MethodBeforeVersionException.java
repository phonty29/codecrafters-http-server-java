package exceptions;

public class MethodBeforeVersionException extends RuntimeException {

  public MethodBeforeVersionException(String message) {
    super(
        "HTTP method is attempted to set before "
    );
  }
}
