import io.HttpRequest;
import io.HttpResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
  private final Socket socket;

  public ConnectionHandler(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try (
        BufferedReader inReader = new BufferedReader(
            new InputStreamReader(this.socket.getInputStream()));
        BufferedOutputStream outWriter = new BufferedOutputStream(this.socket.getOutputStream())
    ) {
      while (!this.socket.isClosed()) {
        HttpRequest httpRequest = HttpRequest.builder().fromReader(inReader).build();
        HttpResponse response = new HttpRequestHandler(httpRequest).handle();
        outWriter.write(response.compiled());
        outWriter.flush();

        if (httpRequest.doCloseConnection()) {
          System.out.println("Connection closed");
          break;
        }
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    } finally {
      try {
        this.socket.close();
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
