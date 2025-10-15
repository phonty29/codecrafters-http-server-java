package handlers;

import enums.HttpStatusCode;
import enums.HttpVersion;
import io.HttpRequest;
import io.HttpResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler implements IHttpRequestHandler {

  @Override
  public HttpResponse handle(HttpRequest request) {
    String filename = request.getRequestURI().substring("/file/".length());
    String filePath = String.format("/tmp/%s", filename);
    StringBuilder bodyBuilder = new StringBuilder();
    try (
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath))
        ) {
      String line;
      while ((line = fileReader.readLine()) != null) {
        bodyBuilder.append(line);
      }
    } catch (IOException e) {
      System.out.println("FileHandler.handle " + e.getMessage());
    }

    return HttpResponse
        .builder()
        .statusCode(HttpStatusCode.SUCCESS)
        .version(HttpVersion.HTTP_1_1)
        .addHeader("content-type", "application/octet-stream")
        .messageBody(bodyBuilder.toString())
        .build();
  }
}
