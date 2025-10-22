import enums.HttpStatusCode;
import handlers.EchoHandler;
import handlers.FileHandler;
import handlers.IHttpRequestHandler;
import handlers.UserAgentHandler;
import io.HttpRequest;
import io.HttpResponse;
import io.HttpResponse.HttpResponseBuilder;

public class HttpRequestHandler implements IHttpRequestHandler {
  private final HttpRequest request;
  private final HttpResponseBuilder httpResponseBuilder;

  public HttpRequestHandler(HttpRequest request) {
    this.request = request;
    this.httpResponseBuilder = HttpResponse
        .builder()
        .version(this.request.getHttpVersion())
        .compressionScheme(this.request.compressionScheme())
        .closeConnection(this.request.doCloseConnection());
  }

  @Override
  public HttpResponse handle() {
    return dispatch();
  }

  private HttpResponse dispatch() {
    return switch (this.request.getRequestURI()) {
      case "/" -> this.httpResponseBuilder
          .statusCode(HttpStatusCode.SUCCESS)
          .build();
      case String s when s.startsWith("/echo/") ->
          new EchoHandler(this.request, this.httpResponseBuilder).handle();
      case "/user-agent" -> new UserAgentHandler(this.request, this.httpResponseBuilder).handle();
      case String s when s.startsWith("/files/") ->
          new FileHandler(this.request, this.httpResponseBuilder).handle();
      default -> this.httpResponseBuilder
          .statusCode(HttpStatusCode.NOT_FOUND)
          .build();
    };
  }
}
