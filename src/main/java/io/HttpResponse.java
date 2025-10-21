package io;

import enums.CompressionScheme;
import enums.HttpStatusCode;
import enums.HttpVersion;
import exceptions.GZIPCompressionException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import utils.GZIPCompressor;

public class HttpResponse {
  // Response status line
  private HttpVersion version;
  private HttpStatusCode statusCode;
  // Response headers
  private final Map<String, String> headers = new HashMap<>();
  // Response message body
  private byte[] messageBody = new byte[0];

  private final static String CONTENT_ENCODING = "content-encoding";
  private final static String CONTENT_LENGTH = "content-length";

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

  private void removeHeader(String key) {
    this.headers.remove(key);
  }

  private void setMessageBody(byte[] body) {
    this.messageBody = body;
  }

  private void setMessageBody(String body) {
    this.messageBody = body.getBytes(StandardCharsets.UTF_8);
  }

  public String compiled() {
    return this.compileStatusLine() + this.compileHeaders() + Arrays.toString(this.messageBody);
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

  private void setContentLength(int length) {
    this.headers.put(CONTENT_LENGTH, String.valueOf(length));
  }

  private void setContentEncoding(CompressionScheme scheme) {
    this.headers.put(CONTENT_ENCODING, scheme.getName());
  }

  private Optional<CompressionScheme> compressionScheme() {
    if (this.headers.containsKey(CONTENT_ENCODING)) {
      String schemeName = this.headers.get(CONTENT_ENCODING);
      return CompressionScheme.fromName(schemeName);
    }

    return Optional.empty();
  }

  public static class HttpResponseBuilder {
    private final HttpResponse httpResponse = new HttpResponse();

    public HttpResponse build() {
      validateHttpResponse();
      if (this.httpResponse.compressionScheme().isEmpty() || this.httpResponse.messageBody.length == 0) {
        this.httpResponse.removeHeader(CONTENT_ENCODING);
      }
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

    public HttpResponseBuilder compressionScheme(Optional<CompressionScheme> scheme) {
      scheme.ifPresent(this.httpResponse::setContentEncoding);
      return this;
    }

    public HttpResponseBuilder messageBody(String body) {
      var optCompressionScheme = this.httpResponse.compressionScheme();
      // REFACTOR! with Pattern Strategy
      if (optCompressionScheme.isPresent() && optCompressionScheme.get().equals(CompressionScheme.GZIP)) {
        byte[] compressedBody = GZIPCompressor.compress(body);
        this.httpResponse.setMessageBody(compressedBody);
        this.httpResponse.setContentLength(compressedBody.length);
      } else {
        this.httpResponse.setMessageBody(body);
        this.httpResponse.setContentLength(body.length());
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
