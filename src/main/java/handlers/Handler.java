package handlers;

import io.HttpRequest;
import io.HttpResponse;
import io.HttpResponse.HttpResponseBuilder;

public interface Handler {

  HttpResponse handle(HttpRequest request);

  default HttpResponseBuilder prepareResponse(HttpRequest request) {
    return HttpResponse
        .builder()
        .version(request.getHttpVersion())
        .compressionScheme(request.compressionScheme())
        .closeConnection(request.doCloseConnection());
  }
}