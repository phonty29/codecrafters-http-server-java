package handlers;

import io.HttpRequest;
import io.HttpResponse;

public interface IHttpRequestHandler {
  HttpResponse handle(HttpRequest request);
}