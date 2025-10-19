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

  public HttpRequestHandler(HttpRequest request) {
    this.request = request;
  }

  @Override
  public HttpResponse handle() {
    return borrowFromRequest().dispatch();
  }

  private HttpRequestDispatcher borrowFromRequest() {
    var builder = HttpResponse
        .builder()
        .version(this.request.getHttpVersion());
    this.request.compressionScheme().ifPresent(builder::compressionScheme);
    return new HttpRequestDispatcher(this.request, builder);
  }

  public static class HttpRequestDispatcher {

    private final HttpRequest request;
    private final HttpResponseBuilder httpResponseBuilder;

    public HttpRequestDispatcher(HttpRequest request, HttpResponseBuilder httpResponseBuilder) {
      this.request = request;
      this.httpResponseBuilder = httpResponseBuilder;
    }

    public HttpResponse dispatch() {
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
}
