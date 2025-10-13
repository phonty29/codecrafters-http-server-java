import enums.HttpStatusCode;
import enums.HttpVersion;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Main {
  private final static String EMPTY_TARGET = "/";
  private final static String EMPTY_STRING = "";

  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage

     try {
       ServerSocket serverSocket = new ServerSocket(4221);

       // Since the tester restarts your program quite often, setting SO_REUSEADDR
       // ensures that we don't run into 'Address already in use' errors
       serverSocket.setReuseAddress(true);

       Socket sock = serverSocket.accept(); // Wait for connection from client. Returns socket.

       HttpRequest httpRequest = HttpRequest.builder().fromInputStream(sock.getInputStream()).build();
       String value = httpRequest.getHeaderValue("user-agent");

       var httpResponseBuilder = HttpResponse.builder().version(HttpVersion.HTTP_1_1);
       HttpResponse response;
       if (Objects.nonNull(value)) {
         response = httpResponseBuilder
             .statusCode(HttpStatusCode.SUCCESS)
             .addHeader("content-type", "text/plain")
             .addHeader("content-length", String.valueOf(value.length()))
             .messageBody(value)
             .build();
       } else {
         response = httpResponseBuilder
             .statusCode(HttpStatusCode.NOT_FOUND)
             .build();
       }

       System.out.println("Response compiled: " + response.compiled());
       PrintWriter sockOutWriter = new PrintWriter(sock.getOutputStream(), true);
       sockOutWriter.println(response.compiled());
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
