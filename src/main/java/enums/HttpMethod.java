package enums;

public enum HttpMethod {
  GET("GET"),
  HEAD("HEAD"),
  POST("POST"),
  PUT("PUT"),
  PATCH("PATCH"),
  DELETE("DELETE"),
  OPTION("OPTION"),
  CONNECT("CONNECT"),
  TRACE("TRACE");

  private final String value;

  HttpMethod(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
