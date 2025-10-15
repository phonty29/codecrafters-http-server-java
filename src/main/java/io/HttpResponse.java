package io;

import enums.HttpStatusCode;
import enums.HttpVersion;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class HttpResponse {
  // Response status line
  private HttpVersion version;
  private HttpStatusCode statusCode;
  // Response headers
  private final Map<String, String> headers = new HashMap<>();
  // Response message body
  private String messageBody = "";

  public static HttpResponseBuilder builder() {
    return new HttpResponseBuilder();
  }

  private void setVersion(HttpVersion version) {
    this.version = version;
  }

  private void setStatusCode(HttpStatusCode code) {
    this.statusCode = code;
  }

  private void addHeader(String key, String value) {
    this.headers.put(key.toLowerCase(), value.toLowerCase());
  }

  private void setMessageBody(String body) {
    this.messageBody = body;
  }

  public String compiled() {
    return this.compileStatusLine() + this.compileHeaders() + this.messageBody;
  }

  private String compileStatusLine() {
    return String.format("%s %d %s\r\n", this.version, this.statusCode.getCode(),
        this.statusCode.getPhrase());
  }

  private String compileHeaders() {
    StringBuilder headersBuilder = new StringBuilder();
    for (var header : this.headers.entrySet()) {
      headersBuilder.append(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
    }
    headersBuilder.append("\r\n");
    return headersBuilder.toString();
  }

  public static class HttpResponseBuilder {
    private final HttpResponse httpResponse = new HttpResponse();

    public HttpResponse build() {
      validateHttpResponse();
      return this.httpResponse;
    }

    public HttpResponseBuilder version(HttpVersion version) {
      this.httpResponse.setVersion(version);
      return this;
    }

    public HttpResponseBuilder statusCode(HttpStatusCode code) {
      this.httpResponse.setStatusCode(code);
      return this;
    }

    public HttpResponseBuilder addHeader(String key, String value) {
      this.httpResponse.addHeader(key, value);
      return this;
    }

    public HttpResponseBuilder messageBody(String body) {
      this.httpResponse.setMessageBody(body);
      if (this.httpResponse.version.isGreaterOrEquals(HttpVersion.HTTP_1_1)) {
        this.httpResponse.addHeader("content-length", String.valueOf(body.length()));
      }
      return this;
    }

    private void validateHttpResponse() {
      if (Stream.of(this.httpResponse.version, this.httpResponse.statusCode)
          .anyMatch(Objects::isNull)) {
        throw new IllegalStateException();
      }
    }
  }
}
