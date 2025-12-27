package enums;

public enum HttpStatusCode {
  SUCCESS(200, "OK"),
  CREATED(201, "Created"),
  METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
  NOT_FOUND(404, "Not Found"),
  BAD_REQUEST(400, "Bad Request"),
  INTERNAL_SERVER_ERROR(500, "Internal Server Error");

  private final int code;
  private final String phrase;

  HttpStatusCode(int code, String phrase) {
    this.code = code;
    this.phrase = phrase;
  }

  public static HttpStatusCode fromCode(int codeNumber) {
    for (var code : HttpStatusCode.values()) {
      if (code.code == codeNumber) {
        return code;
      }
    }
    throw new IllegalArgumentException();
  }

  public int getCode() {
    return this.code;
  }

  public String getPhrase() {
    return this.phrase;
  }
}
