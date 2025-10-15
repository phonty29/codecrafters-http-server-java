package handlers;

import context.GlobalScope;
import enums.HttpStatusCode;
import enums.HttpVersion;
import io.HttpRequest;
import io.HttpResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler implements IHttpRequestHandler {

  @Override
  public HttpResponse handle(HttpRequest request) {
    String filename = request.getRequestURI().substring("/files/".length());
    String filePath = String.format("/%s/%s", GlobalScope.FILE_PARENT_PATH, filename);
    StringBuilder bodyBuilder = new StringBuilder();
    try (
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath))
        ) {
      String line;
      while ((line = fileReader.readLine()) != null) {
        bodyBuilder.append(line);
      }
    } catch (FileNotFoundException e) {
      System.out.println("FileHandler.handle " + e.getMessage());
      return HttpResponse
          .builder()
          .version(HttpVersion.HTTP_1_1)
          .statusCode(HttpStatusCode.NOT_FOUND)
          .build();
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
