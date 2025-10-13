import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import request.HttpRequest;

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

       var httpRequest = HttpRequest.builder().fromInputStream(sock.getInputStream()).build();
       String value = httpRequest.getHeaderValue("user-agent");
       System.out.println("HTTP header 'user-agent': " + value);


//       String httpResponseMessage = buildHTTPResponseMessage(HTTPStatusCode.SUCCESS, content);
//
//       PrintWriter sockOutWriter = new PrintWriter(sock.getOutputStream(), true);
//       sockOutWriter.println(httpResponseMessage);
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }

  private static String buildHTTPResponseMessage(HTTPStatusCode statusCode, String content) {
    switch (statusCode) {
      case NO_CONTENT -> {
        return "HTTP/1.1 200 OK\r\n\r\n";
      }
      case SUCCESS -> {
        return String.format("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: %d\r\n\r\n%s", content.length(), content);
      }
      case NOT_FOUND -> {
        return "HTTP/1.1 404 Not Found\r\n\r\n";
      }
      default -> {
        return "HTTP/1.1 500 Internal Server Error\r\n\r\n";
      }
    }
  }

  private enum HTTPStatusCode {
    SUCCESS,
    NO_CONTENT,
    NOT_FOUND
  }

//  private static String getRequestTarget(String requestMessage) {
//    var matcher = Pattern.compile("GET (.*?) HTTP/1.1").matcher(requestMessage);
//    if (matcher.find()) {
//      String requestTarget = matcher.group(1);
//      if (requestTarget.startsWith(TARGET_START_SEQUENCE) || requestTarget.contentEquals(
//          EMPTY_TARGET)) {
//        return requestTarget;
//      }
//      return EMPTY_STRING;
//    }
//    return EMPTY_STRING;
//  }
//
//  private static String getContentOfRequestTarget(String requestTarget) {
//    int startIndex = TARGET_START_SEQUENCE.length();
//    return requestTarget.substring(startIndex);
//  }
}
