import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

public class Main {
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

       System.out.println("accepted new connection");

       // Read input stream from socket and convert it to String
       String requestMessage, successResponseMessage, notFoundResponseMessage;
       // Then print in standard output

       // An InputStreamReader is a bridge from byte streams to character streams: It reads bytes and decodes them into characters using a specified charset.
       InputStreamReader inStreamReader = new InputStreamReader(sock.getInputStream());
       BufferedReader bufReader = new BufferedReader(inStreamReader);

       requestMessage = bufReader.readLine();
       System.out.println("Request message: " +  requestMessage);
       String desiredRequestTarget = "/";
       String requestTarget = getRequestTarget(requestMessage);

       PrintWriter sockOutWriter = new PrintWriter(sock.getOutputStream(), true);
       successResponseMessage = "HTTP/1.1 200 OK\r\n\r\n";
       notFoundResponseMessage = "HTTP/1.1 404 Not Found\r\n\r\n";

       if (desiredRequestTarget.contentEquals(requestTarget)) {
         sockOutWriter.println(successResponseMessage);
       } else {
         sockOutWriter.println(notFoundResponseMessage);
       }
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }

  private static String getRequestTarget(String requestMessage) {
    var matcher = Pattern.compile("GET (.*?) HTTP/1.1").matcher(requestMessage);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return "";
  }
}
