import enums.HttpStatusCode;
import enums.HttpVersion;

public class HttpRequestHandler implements IHttpRequestHandler {

  @Override
  public HttpResponse handle(HttpRequest request) {
    return pathHandler(request);
  }

  private HttpResponse pathHandler(HttpRequest request) {
    return switch (request.getRequestURI()) {
      case "/" ->
          HttpResponse.builder().statusCode(HttpStatusCode.SUCCESS).version(HttpVersion.HTTP_1_1)
              .build();
      case String s when s.startsWith("/echo/") -> {
        String echo = request.getRequestURI().substring("/echo/".length());
        yield HttpResponse
            .builder()
            .statusCode(HttpStatusCode.SUCCESS)
            .version(HttpVersion.HTTP_1_1)
            .messageBody(echo)
            .build();
      }
      case "/user-agent" -> {
        var userAgent = request.getHeaderValue("user-agent");
        if (userAgent.isPresent()) {
          yield HttpResponse
              .builder()
              .statusCode(HttpStatusCode.SUCCESS)
              .version(HttpVersion.HTTP_1_1)
              .messageBody(userAgent.get())
              .build();
        } else {
          yield HttpResponse
              .builder()
              .statusCode(HttpStatusCode.SUCCESS)
              .version(HttpVersion.HTTP_1_1)
              .messageBody("")
              .build();
        }
      }
      default ->
          HttpResponse.builder().statusCode(HttpStatusCode.NOT_FOUND).version(HttpVersion.HTTP_1_1)
              .build();
    };
  }
}
