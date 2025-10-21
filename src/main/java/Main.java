import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  private static final ExecutorService threadPool;
  static {
    threadPool = Executors.newVirtualThreadPerTaskExecutor();
  }

  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");
    try (ServerSocket serverSocket = new ServerSocket(4221)) {
      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);

      while (true) {
        threadPool.submit(new ConnectionHandler(serverSocket));
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
