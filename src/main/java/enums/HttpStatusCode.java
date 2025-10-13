package enums;

public enum HttpStatusCode {
  SUCCESS(200, "OK"),
  NOT_FOUND(404, "Not Found");

  private final int code;
  private final String phrase;

  HttpStatusCode(int code, String phrase) {
    this.code = code;
    this.phrase = phrase;
  }

  public int getCode() {
    return this.code;
  }

  public String getPhrase() {
    return this.phrase;
  }

  public static HttpStatusCode fromCode(int codeNumber) {
    for (var code : HttpStatusCode.values()) {
      if (code.code == codeNumber) {
        return code;
      }
    }
    throw new IllegalArgumentException();
  }
}
