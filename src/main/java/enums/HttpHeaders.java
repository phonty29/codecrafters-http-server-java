package enums;

public enum HttpHeaders {
  ACCEPT_ENCODING("Accept-Encoding"),
  CONTENT_ENCODING("Content-Encoding"),
  CONTENT_LENGTH("Content-Length"),
  CONNECTION("Connection");

  private final String value;

  HttpHeaders(String value) {
    this.value = value;
  }

  public String value() {
    return this.value.toLowerCase();
  }
}
