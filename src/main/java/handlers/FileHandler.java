package handlers;

import enums.HttpStatusCode;
import enums.HttpVersion;
import io.HttpRequest;
import io.HttpResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler implements IHttpRequestHandler {
  private static final String FILE_PARENT_PATH;
  static {
    FILE_PARENT_PATH = "tmp/data/codecrafters.io/http-server-tester";
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    String filename = request.getRequestURI().substring("/files/".length());
    String filePath = String.format("/%s/%s", FILE_PARENT_PATH, filename);

    return switch (request.getHttpMethod()) {
      case GET -> handleGet(filePath);
      case POST -> handlePost(filePath, request.getMessageBody());
      default -> HttpResponse
          .builder()
          .statusCode(HttpStatusCode.METHOD_NOT_ALLOWED)
          .version(HttpVersion.HTTP_1_1)
          .build();
    };
  }

  private HttpResponse handleGet(String filePath) {
    StringBuilder bodyBuilder = new StringBuilder();
    try (
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath))
    ) {
      String line;
      while ((line = fileReader.readLine()) != null) {
        bodyBuilder.append(line);
      }
    } catch (FileNotFoundException e) {
      System.err.println("FileHandler.handle " + e.getMessage());
      return HttpResponse
          .builder()
          .version(HttpVersion.HTTP_1_1)
          .statusCode(HttpStatusCode.NOT_FOUND)
          .build();
    } catch (IOException e) {
      System.err.println("FileHandler.handle " + e.getMessage());
    }

    return HttpResponse
        .builder()
        .statusCode(HttpStatusCode.SUCCESS)
        .version(HttpVersion.HTTP_1_1)
        .addHeader("content-type", "application/octet-stream")
        .messageBody(bodyBuilder.toString())
        .build();
  }

  private HttpResponse handlePost(String filePath, String messageBody) {
    var responseBuilder = HttpResponse.builder().version(HttpVersion.HTTP_1_1);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      writer.write(messageBody);
      return responseBuilder
          .statusCode(HttpStatusCode.CREATED)
          .build();
    } catch (IOException e) {
      System.err.println("FileHandler.handle " + e.getMessage());
    }

    return responseBuilder
        .statusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
        .build();
  }
}
