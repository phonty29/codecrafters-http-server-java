import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

public class Main {
  private final static String EMPTY_TARGET = "/";
  private final static String EMPTY_STRING = "";
  private final static String TARGET_START_SEQUENCE = "/echo/";

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

       // Read input stream from socket and convert it to String
       String httpRequestMessage, httpResponseMessage;

       httpRequestMessage = getHTTPRequestMessageFromSocket(sock);
       String requestTarget = getRequestTarget(httpRequestMessage);
       if (requestTarget.contentEquals(EMPTY_TARGET)) {
         httpResponseMessage = buildHTTPResponseMessage(HTTPStatusCode.NO_CONTENT, EMPTY_STRING);
       } else if (requestTarget.contentEquals(EMPTY_STRING)) {
         httpResponseMessage = buildHTTPResponseMessage(HTTPStatusCode.NOT_FOUND, EMPTY_STRING);
       } else {
         String content = getContent(requestTarget);
         httpResponseMessage = buildHTTPResponseMessage(HTTPStatusCode.SUCCESS, content);
       }

       PrintWriter sockOutWriter = new PrintWriter(sock.getOutputStream(), true);
       sockOutWriter.println(httpResponseMessage);
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }

  private static String getHTTPRequestMessageFromSocket(Socket sock) throws IOException {
    // An InputStreamReader is a bridge from byte streams to character streams: It reads bytes and decodes them into characters using a specified charset.
    InputStreamReader inStreamReader = new InputStreamReader(sock.getInputStream());
    BufferedReader bufReader = new BufferedReader(inStreamReader);
    return bufReader.readLine();
  }

  private static String buildHTTPResponseMessage(HTTPStatusCode statusCode, String content) {
    switch (statusCode) {
      case NO_CONTENT -> {
        return "HTTP/1.1 200 OK\r\n\r\n";
      }
      case SUCCESS -> {
        return String.format("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: %d\r\n\r%s", content.length(), content);
      }
      case NOT_FOUND -> {
        return "HTTP/1.1 404 Not Found\r\n\r\n";
      }
      default -> {
        return "HTTP/1.1 500 Internal Server Error\r\n\r\n";
      }
    }
  }

  private static String getRequestTarget(String requestMessage) {
    var matcher = Pattern.compile("GET (.*?) HTTP/1.1").matcher(requestMessage);
    if (matcher.find()) {
      String requestTarget = matcher.group(1);
      if (requestTarget.startsWith(TARGET_START_SEQUENCE)) {
        return requestTarget;
      }
      return EMPTY_STRING;
    }
    return EMPTY_STRING;
  }

  private static String getContent(String requestTarget) {
    int startIndex = TARGET_START_SEQUENCE.length();
    return requestTarget.substring(startIndex);
  }

  private enum HTTPStatusCode {
    SUCCESS,
    NO_CONTENT,
    NOT_FOUND
  }
}
