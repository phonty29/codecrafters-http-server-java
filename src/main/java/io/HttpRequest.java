package io;

import static enums.HttpHeaders.ACCEPT_ENCODING;
import static enums.HttpHeaders.CONNECTION;
import static enums.HttpHeaders.CONTENT_LENGTH;

import enums.CompressionScheme;
import enums.HttpMethod;
import enums.HttpVersion;
import exceptions.HttpIncorrectHeaderFormat;
import exceptions.HttpMethodNotSupported;
import exceptions.HttpRequestLineLengthException;
import exceptions.HttpVersionNotSupported;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class HttpRequest {
  // Request Line
  private HttpMethod httpMethod;
  private String requestURI = "";
  private HttpVersion httpVersion;
  // Request Headers
  private final Map<String, String> headers = new HashMap<>();
  // Optional message body
  private String messageBody;

  public static HttpRequestBuilder builder() {
    return new HttpRequestBuilder();
  }

  void setRequestLine(String method, String uri, String version) {
    this.setHttpVersion(version);
    this.setHttpMethod(method);
    this.setRequestURI(uri);
  }

  void addHeader(String key, String value) {
    this.headers.put(key.toLowerCase(), value.toLowerCase());
  }

  void setMessageBody(String messageBody) {
    this.messageBody = messageBody;
  }

  private void setHttpVersion(HttpVersion version) {
    this.httpVersion = version;
  }

  private void setHttpVersion(String version) {
    try {
      this.setHttpVersion(HttpVersion.fromValue(version));
    } catch (IllegalArgumentException ex) {
      throw new HttpVersionNotSupported(version);
    }
  }

  private void setHttpMethod(HttpMethod method) {
    if (Objects.nonNull(this.httpVersion)) {
      this.httpMethod = method;
    } else {
      throw new IllegalStateException("HTTP version should go before HTTP method");
    }
  }

  private void setHttpMethod(String method) {
    try {
      this.setHttpMethod(HttpMethod.valueOf(method));
      if (Objects.nonNull(this.httpVersion) && !this.httpVersion.supportsMethod(this.httpMethod)) {
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException ex) {
      throw new HttpMethodNotSupported(this.httpVersion, method);
    }
  }

  private void setRequestURI(String requestURI) {
    this.requestURI = requestURI;
  }

  public HttpMethod getHttpMethod() {
    return this.httpMethod;
  }

  public HttpVersion getHttpVersion() {
    return this.httpVersion;
  }

  public String getRequestURI() {
    return this.requestURI;
  }

  public String getMessageBody() {
    return this.messageBody;
  }

  public Optional<String> getHeaderValue(String key) {
    if (this.headers.containsKey(key.toLowerCase())) {
      return Optional.of(this.headers.get(key));
    }
    return Optional.empty();
  }

  public Optional<CompressionScheme> compressionScheme() {
    if (this.headers.containsKey(ACCEPT_ENCODING.value())) {
      var schemes = Arrays.stream(this.headers.get(ACCEPT_ENCODING.value()).split(",")).map(
          String::trim).toList();
      for (String scheme : schemes) {
        if (CompressionScheme.supports(scheme)) {
          return CompressionScheme.fromName(scheme);
        }
      }
    }

    return Optional.empty();
  }

  public boolean doCloseConnection() {
    var connectionHeader = this.getHeaderValue(CONNECTION.value());
    return connectionHeader.map(s -> s.equals("close")).orElse(false);
  }

  public static class HttpRequestBuilder {
    private final static int REQUEST_LINE_LENGTH = 3;

    private final HttpRequest httpRequest = new HttpRequest();
    private BufferedReader reader;

    public HttpRequestBuilder fromReader(BufferedReader reader) throws IOException {
      this.reader = reader;
      String line = this.reader.readLine();
      this.setRequestLine(line);
      this.setHeaders();
      this.setMessageBody();
      return this;
    }

    public HttpRequest build() {
      validateHttpRequestState();
      return this.httpRequest;
    }

    private void validateHttpRequestState() {
      if (Stream.of(httpRequest.getHttpMethod(), httpRequest.getHttpVersion(),
          httpRequest.getRequestURI()).anyMatch(Objects::isNull)) {
        throw new IllegalStateException();
      }
    }

    private void setRequestLine(String requestLine) {
      String[] splitRequestLine = requestLine.split(" ");
      if (splitRequestLine.length != REQUEST_LINE_LENGTH) {
        throw new HttpRequestLineLengthException(splitRequestLine.length);
      }
      String method = splitRequestLine[0], requestUri = splitRequestLine[1], version = splitRequestLine[2];
      this.httpRequest.setRequestLine(method, requestUri, version);
    }

    private void setHeaders() throws IOException {
      String line;
      while ((line = this.reader.readLine()) != null && !line.isEmpty()) {
        int idx = line.indexOf(":");
        if (idx == -1) {
          throw new HttpIncorrectHeaderFormat(line);
        }
        String key = line.substring(0, idx).trim().toLowerCase(),
            value = line.substring(idx+1).trim();
        this.httpRequest.addHeader(key, value);
      }
    }

    private void setMessageBody() throws IOException {
      StringBuilder bodyBuilder = new StringBuilder();
      var optContentLength = this.httpRequest.getHeaderValue(CONTENT_LENGTH.value());
      if (optContentLength.isPresent()) {
        int contentLength = Integer.parseInt(optContentLength.get());
        if (contentLength > 0) {
          char[] charBuf = new char[contentLength];
          int charRead = this.reader.read(charBuf);
          if (charRead > 0) {
            bodyBuilder.append(charBuf);
          }
        }
      }
      this.httpRequest.setMessageBody(bodyBuilder.toString());
    }
  }
}
