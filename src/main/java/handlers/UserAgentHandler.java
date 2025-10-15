package handlers;

import enums.HttpStatusCode;
import enums.HttpVersion;
import io.HttpRequest;
import io.HttpResponse;

public class UserAgentHandler implements IHttpRequestHandler {

  @Override
  public HttpResponse handle(HttpRequest request) {
    var userAgent = request.getHeaderValue("user-agent");
    if (userAgent.isPresent()) {
      return HttpResponse
          .builder()
          .statusCode(HttpStatusCode.SUCCESS)
          .version(HttpVersion.HTTP_1_1)
          .messageBody(userAgent.get())
          .addHeader("content-type", "text/plain")
          .build();
    } else {
      return HttpResponse
          .builder()
          .statusCode(HttpStatusCode.SUCCESS)
          .version(HttpVersion.HTTP_1_1)
          .messageBody("")
          .addHeader("content-type", "text/plain")
          .build();
    }
  }
}
