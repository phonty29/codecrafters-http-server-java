package handlers;

import enums.HttpStatusCode;
import enums.HttpVersion;
import io.HttpRequest;
import io.HttpResponse;

public class UserAgentHandler implements IHttpRequestHandler {

  @Override
  public HttpResponse handle(HttpRequest request) {
    var userAgent = request.getHeaderValue("user-agent");
    var builder = HttpResponse
        .builder()
        .statusCode(HttpStatusCode.SUCCESS)
        .version(HttpVersion.HTTP_1_1)
        .addHeader("content-type", "text/plain");

    if (userAgent.isPresent()) {
      builder = builder
          .messageBody(userAgent.get());
    } else {
      builder = builder
          .messageBody("");
    }

    if (request.compressionScheme().isPresent()) {
      builder = builder
          .compressionScheme(request.compressionScheme().get());
    }

    return builder.build();
  }
}
