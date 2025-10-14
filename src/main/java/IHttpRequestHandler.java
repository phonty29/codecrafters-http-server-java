public interface IHttpRequestHandler {
  HttpResponse handle(HttpRequest request);
}