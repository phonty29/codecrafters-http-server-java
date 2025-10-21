package handlers;

import enums.HttpStatusCode;
import io.HttpRequest;
import io.HttpResponse;
import io.HttpResponse.HttpResponseBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler implements IHttpRequestHandler {
  private final HttpRequest request;
  private final HttpResponseBuilder httpResponseBuilder;
  private static final String FILE_PARENT_PATH;
  static {
    FILE_PARENT_PATH = "tmp/data/codecrafters.io/http-server-tester";
  }

  public FileHandler(HttpRequest request, HttpResponseBuilder httpResponseBuilder) {
    this.request = request;
    this.httpResponseBuilder = httpResponseBuilder;
  }

  @Override
  public HttpResponse handle() {
    String filename = this.request.getRequestURI().substring("/files/".length());
    String filePath = String.format("/%s/%s", FILE_PARENT_PATH, filename);

    return switch (this.request.getHttpMethod()) {
      case GET -> handleGet(filePath);
      case POST -> handlePost(filePath);
      default -> this.httpResponseBuilder
          .statusCode(HttpStatusCode.METHOD_NOT_ALLOWED)
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
      return this.httpResponseBuilder
          .statusCode(HttpStatusCode.NOT_FOUND)
          .build();
    } catch (IOException e) {
      System.err.println("FileHandler.handle " + e.getMessage());
    }

    return this.httpResponseBuilder
        .statusCode(HttpStatusCode.SUCCESS)
        .addHeader("content-type", "application/octet-stream")
        .messageBody(bodyBuilder.toString())
        .build();
  }

  private HttpResponse handlePost(String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      writer.write(this.request.getMessageBody());
      return this.httpResponseBuilder
          .statusCode(HttpStatusCode.CREATED)
          .build();
    } catch (IOException e) {
      System.err.println("FileHandler.handle " + e.getMessage());
    }

    return this.httpResponseBuilder
        .statusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
        .build();
  }
}
