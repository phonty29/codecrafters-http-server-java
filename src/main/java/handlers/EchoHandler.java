package handlers;

import enums.HttpStatusCode;
import io.HttpRequest;
import io.HttpResponse;
import io.HttpResponse.HttpResponseBuilder;

public class EchoHandler implements IHttpRequestHandler {
  private final HttpRequest request;
  private final HttpResponseBuilder httpResponseBuilder;

  public EchoHandler(HttpRequest request, HttpResponseBuilder httpResponseBuilder) {
    this.request = request;
    this.httpResponseBuilder = httpResponseBuilder;
  }

  @Override
  public HttpResponse handle() {
    String body = this.request.getRequestURI().substring("/echo/".length());
    return httpResponseBuilder
        .addHeader("content-type", "text/plain")
        .statusCode(HttpStatusCode.SUCCESS)
        .messageBody(body)
        .build();
  }
}
