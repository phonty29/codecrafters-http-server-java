package handlers;

import enums.HttpStatusCode;
import io.HttpRequest;
import io.HttpResponse;

public class EchoHandler implements Handler {

  @Override
  public HttpResponse handle(HttpRequest request) {
    String body = request.getRequestURI().substring("/echo/".length());
    return prepareResponse(request)
        .addHeader("content-type", "text/plain")
        .statusCode(HttpStatusCode.SUCCESS)
        .messageBody(body)
        .build();
  }
}
