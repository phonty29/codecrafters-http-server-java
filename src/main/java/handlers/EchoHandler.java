package handlers;

import enums.HttpStatusCode;
import enums.HttpVersion;
import io.HttpRequest;
import io.HttpResponse;

public class EchoHandler implements IHttpRequestHandler {

  @Override
  public HttpResponse handle(HttpRequest request) {
    String echo = request.getRequestURI().substring("/echo/".length());
    return HttpResponse
        .builder()
        .statusCode(HttpStatusCode.SUCCESS)
        .version(HttpVersion.HTTP_1_1)
        .messageBody(echo)
        .addHeader("content-type", "text/plain")
        .build();
  }
}
