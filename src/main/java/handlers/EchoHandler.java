package handlers;

import enums.HttpStatusCode;
import enums.HttpVersion;
import io.HttpRequest;
import io.HttpResponse;

public class EchoHandler implements IHttpRequestHandler {

  @Override
  public HttpResponse handle(HttpRequest request) {
    String body = request.getRequestURI().substring("/echo/".length());
    var builder = HttpResponse
        .builder()
        .addHeader("content-type", "text/plain")
        .statusCode(HttpStatusCode.SUCCESS)
        .version(HttpVersion.HTTP_1_1)
        .messageBody(body);

    if (request.compressionScheme().isPresent()) {
      builder = builder
          .compressionScheme(request.compressionScheme().get());
    }
    return builder.build();
  }
}
