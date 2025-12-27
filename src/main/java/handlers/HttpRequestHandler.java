package handlers;

import enums.HttpStatusCode;
import io.HttpRequest;
import io.HttpResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class HttpRequestHandler implements Handler {
  private final List<Route> routes = List.of(
      new Route(p -> p.equals("/"), (r) -> prepareResponse(r).statusCode(HttpStatusCode.SUCCESS).build()),
      new Route(p -> p.startsWith("/echo"), new EchoHandler()),
      new Route(p -> p.equals("/user-agent"), new UserAgentHandler()),
      new Route(p -> p.startsWith("/files"), new FileHandler())
  );

  @Override
  public HttpResponse handle(HttpRequest request) {
    String path = normalizePath(request.getRequestURI());
    if (Objects.isNull(path)) {
      prepareResponse(request)
          .statusCode(HttpStatusCode.BAD_REQUEST)
          .build();
    }

    for (var route : routes) {
      if (route.match().test(path)) {
        return route.handler().handle(request);
      }
    }

    return prepareResponse(request)
        .statusCode(HttpStatusCode.NOT_FOUND)
        .build();
  }

  private String normalizePath(String rawUri) {
    int q = rawUri.indexOf('?');
    String path = (q >= 0) ? rawUri.substring(0, q) : rawUri;
    try {
      path = URLDecoder.decode(path, StandardCharsets.UTF_8);
    } catch (IllegalArgumentException e) {
      return null; // malformed encoding → 400
    }
    if (!path.startsWith("/")) {
      return null;
    }
    // Prevent directory traversal
    if (path.contains("..")) {
      return null;
    }
    return path;
  }
}
