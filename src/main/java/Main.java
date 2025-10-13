import enums.HttpStatusCode;
import enums.HttpVersion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Main {

  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage

    try (ServerSocket serverSocket = new ServerSocket(4221)) {
       // Since the tester restarts your program quite often, setting SO_REUSEADDR
       // ensures that we don't run into 'Address already in use' errors
       serverSocket.setReuseAddress(true);

       Socket sock = serverSocket.accept(); // Wait for connection from client. Returns socket.

      while (!sock.isClosed()) {
        BufferedReader inReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        PrintWriter sockOutWriter = new PrintWriter(sock.getOutputStream(), true);

        HttpRequest httpRequest = HttpRequest.builder().fromReader(inReader).build();
        var userAgentValue = httpRequest.getHeaderValue("user-agent");

        var httpResponseBuilder = HttpResponse.builder().version(HttpVersion.HTTP_1_1);
        HttpResponse response;
        if (userAgentValue.isPresent()) {
          response = httpResponseBuilder
              .statusCode(HttpStatusCode.SUCCESS)
              .addHeader("content-type", "text/plain")
              .addHeader("content-length", String.valueOf(userAgentValue.get().length()))
              .messageBody(userAgentValue.get())
              .build();
        } else {
          response = httpResponseBuilder
              .statusCode(HttpStatusCode.NOT_FOUND)
              .build();
        }

        sockOutWriter.println(response.compiled());
        inReader.close();
      }

     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
       throw new RuntimeException(e);
     }
  }
}
