import io.HttpRequest;
import io.HttpResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
  private final Socket socket;
  private boolean keepAlive = true;

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
      while (this.keepAlive) {
        HttpRequest httpRequest = HttpRequest.builder().fromReader(inReader).build();
        this.keepAlive = !httpRequest.doCloseConnection();
        HttpResponse response = new HttpRequestHandler(httpRequest).handle();
        System.out.println(response.compiled().length);
        outWriter.write(response.compiled());
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
