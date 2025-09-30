import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
       String requestMessage, responseMessage;
       // Then print in standard output

       InputStream inStream = sock.getInputStream();
       // An InputStreamReader is a bridge from byte streams to character streams: It reads bytes and decodes them into characters using a specified charset.
       InputStreamReader inStreamReader = new InputStreamReader(inStream);
       BufferedReader bufReader = new BufferedReader(inStreamReader);
       requestMessage = bufReader.readLine();

       System.out.println("Received message:" + " " + requestMessage);
       PrintWriter sockOutWriter = new PrintWriter(sock.getOutputStream(), true);
       responseMessage = "HTTP/1.1 200 OK\\r\\n\\r\\n\n";
       System.out.println("Response message:" + " " + responseMessage);
       sockOutWriter.println();
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
