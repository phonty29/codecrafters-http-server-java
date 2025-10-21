import io.HttpRequest;
import io.HttpResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
  private final ServerSocket serverSocket;

  public ConnectionHandler(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  @Override
  public void run() {
    try {
      Socket sock = serverSocket.accept();
      while (sock.isConnected() && !sock.isClosed()) {
          BufferedReader inReader = new BufferedReader(
              new InputStreamReader(sock.getInputStream()));
          BufferedOutputStream outWriter = new BufferedOutputStream(sock.getOutputStream());
          HttpRequest httpRequest = HttpRequest.builder().fromReader(inReader).build();
          HttpResponse response = new HttpRequestHandler(httpRequest).handle();
          outWriter.write(response.compiled());
          if (httpRequest.doCloseConnection()) {
            sock.close();
          }
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
      Thread.currentThread().interrupt();
    }
  }
}
