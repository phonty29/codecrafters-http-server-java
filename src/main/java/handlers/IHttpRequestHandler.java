package handlers;

import io.HttpResponse;

public interface IHttpRequestHandler {
  HttpResponse handle();
}