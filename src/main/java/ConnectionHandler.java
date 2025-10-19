import io.HttpRequest;
import io.HttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
  private final Socket socket;

  public ConnectionHandler(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try (
        this.socket;
        BufferedReader inReader = new BufferedReader(
            new InputStreamReader(this.socket.getInputStream()));
        PrintWriter sockOutWriter = new PrintWriter(this.socket.getOutputStream(), true)
    ) {
      HttpRequest httpRequest = HttpRequest.builder().fromReader(inReader).build();
      HttpResponse response = new HttpRequestHandler(httpRequest).handle();
      sockOutWriter.println(response.compiled());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
