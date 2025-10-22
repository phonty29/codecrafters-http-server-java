package io;

import static enums.HttpHeaders.CONNECTION;
import static enums.HttpHeaders.CONTENT_ENCODING;
import static enums.HttpHeaders.CONTENT_LENGTH;

import enums.CompressionScheme;
import enums.HttpStatusCode;
import enums.HttpVersion;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import utils.CompressorService;

public class HttpResponse {
  // Response status line
  private HttpVersion version;
  private HttpStatusCode statusCode;
  // Response headers
  private final Map<String, String> headers = new HashMap<>();
  // Response message body
  private byte[] messageBody = new byte[0];

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

  public byte[] compiled() {
    byte[] statusLine = this.compileStatusLine();
    byte[] headers = this.compileHeaders();
    return ByteBuffer.allocate(statusLine.length + headers.length + this.messageBody.length)
        .put(statusLine)
        .put(headers)
        .put(this.messageBody)
        .array();
  }

  private byte[] compileStatusLine() {
    return String.format("%s %d %s\r\n", this.version, this.statusCode.getCode(),
        this.statusCode.getPhrase()).getBytes();
  }

  private byte[] compileHeaders() {
    StringBuilder headersBuilder = new StringBuilder();
    for (var header : this.headers.entrySet()) {
      headersBuilder.append(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
    }
    headersBuilder.append("\r\n");
    return headersBuilder.toString().getBytes();
  }

  private void setContentLength(int length) {
    this.headers.put(CONTENT_LENGTH.value(), String.valueOf(length));
  }

  private void setContentEncoding(CompressionScheme scheme) {
    this.headers.put(CONTENT_ENCODING.value(), scheme.getName());
  }

  private Optional<CompressionScheme> compressionScheme() {
    if (this.headers.containsKey(CONTENT_ENCODING.value())) {
      String schemeName = this.headers.get(CONTENT_ENCODING.value());
      return CompressionScheme.fromName(schemeName);
    }

    return Optional.empty();
  }

  private void closeConnection() {
    this.headers.put(CONNECTION.value(), "close");
  }

  public static class HttpResponseBuilder {
    private final HttpResponse httpResponse = new HttpResponse();

    public HttpResponse build() {
      validateHttpResponse();
      if (this.httpResponse.compressionScheme().isEmpty() || this.httpResponse.messageBody.length == 0) {
        this.httpResponse.removeHeader(CONTENT_ENCODING.value());
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

    public HttpResponseBuilder closeConnection(boolean doClose) {
      System.out.println("closeConnection: " + doClose);
      if (doClose) {
        this.httpResponse.closeConnection();
      }
      return this;
    }

    public HttpResponseBuilder messageBody(String body) {
      var optCompressionScheme = this.httpResponse.compressionScheme();
      // REFACTOR! with Pattern Strategy
      if (optCompressionScheme.isPresent()) {
        byte[] compressedBody = CompressorService.compress(optCompressionScheme.get(), body);
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
