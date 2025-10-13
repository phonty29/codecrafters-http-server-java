package exceptions;

public class HttpIncorrectHeaderFormat extends RuntimeException {

  public HttpIncorrectHeaderFormat(String value) {
    super(String.format("Line %s is not a HTTP header", value));
  }
}
