import enums.HttpStatusCode;
import enums.HttpVersion;
import handlers.EchoHandler;
import handlers.FileHandler;
import handlers.IHttpRequestHandler;
import handlers.UserAgentHandler;
import io.HttpRequest;
import io.HttpResponse;

public class HttpRequestHandler implements IHttpRequestHandler {
  private final EchoHandler echoHandler = new EchoHandler();
  private final UserAgentHandler userAgentHandler = new UserAgentHandler();
  private final FileHandler fileHandler = new FileHandler();

  @Override
  public HttpResponse handle(HttpRequest request) {
    return dispatch(request);
  }

  private HttpResponse dispatch(HttpRequest request) {
    return switch (request.getRequestURI()) {
      case "/" ->
          HttpResponse.builder().statusCode(HttpStatusCode.SUCCESS).version(HttpVersion.HTTP_1_1)
              .build();
      case String s when s.startsWith("/echo/") -> echoHandler.handle(request);
      case "/user-agent" -> userAgentHandler.handle(request);
      case String s when s.startsWith("/files/") -> fileHandler.handle(request);
      default ->
          HttpResponse.builder().statusCode(HttpStatusCode.NOT_FOUND).version(HttpVersion.HTTP_1_1)
              .build();
    };
  }
}
