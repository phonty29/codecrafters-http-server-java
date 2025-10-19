package handlers;

import enums.HttpStatusCode;
import io.HttpRequest;
import io.HttpResponse;
import io.HttpResponse.HttpResponseBuilder;

public class UserAgentHandler implements IHttpRequestHandler {
  private final HttpRequest request;
  private final HttpResponseBuilder httpResponseBuilder;

  public UserAgentHandler(HttpRequest request, HttpResponseBuilder httpResponseBuilder) {
    this.request = request;
    this.httpResponseBuilder = httpResponseBuilder;
  }

  @Override
  public HttpResponse handle() {
    var userAgent = this.request.getHeaderValue("user-agent");
    var builder = this.httpResponseBuilder
        .statusCode(HttpStatusCode.SUCCESS)
        .addHeader("content-type", "text/plain");
    userAgent.ifPresent(builder::messageBody);
    return builder.build();
  }
}
