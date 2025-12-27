package handlers;

import enums.HttpStatusCode;
import io.HttpRequest;
import io.HttpResponse;

public class UserAgentHandler implements Handler {

  @Override
  public HttpResponse handle(HttpRequest request) {
    var userAgent = request.getHeaderValue("user-agent");
    var builder = prepareResponse(request)
        .statusCode(HttpStatusCode.SUCCESS)
        .addHeader("content-type", "text/plain");
    userAgent.ifPresent(builder::messageBody);
    return builder.build();
  }
}
