package handlers;

import enums.HttpStatusCode;
import io.HttpRequest;
import io.HttpResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler implements Handler {

  private static final String FILE_PARENT_PATH;

  static {
    FILE_PARENT_PATH = "tmp/data/codecrafters.io/http-server-tester";
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    String filename = request.getRequestURI().substring("/files/".length());
    String filePath = String.format("/%s/%s", FILE_PARENT_PATH, filename);

    return switch (request.getHttpMethod()) {
      case GET -> handleGet(request, filePath);
      case POST -> handlePost(request, filePath);
      default -> prepareResponse(request)
          .statusCode(HttpStatusCode.METHOD_NOT_ALLOWED)
          .build();
    };
  }

  private HttpResponse handleGet(HttpRequest request, String filePath) {
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
      return prepareResponse(request)
          .statusCode(HttpStatusCode.NOT_FOUND)
          .build();
    } catch (IOException e) {
      System.err.println("FileHandler.handle " + e.getMessage());
    }

    return prepareResponse(request)
        .statusCode(HttpStatusCode.SUCCESS)
        .addHeader("content-type", "application/octet-stream")
        .messageBody(bodyBuilder.toString())
        .build();
  }

  private HttpResponse handlePost(HttpRequest request, String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      writer.write(request.getMessageBody());
      return prepareResponse(request)
          .statusCode(HttpStatusCode.CREATED)
          .build();
    } catch (IOException e) {
      System.err.println("FileHandler.handle " + e.getMessage());
    }

    return prepareResponse(request)
        .statusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
        .build();
  }
}
