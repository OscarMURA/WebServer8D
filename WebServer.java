import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servidor web multi-hilos con ThreadPool para manejo eficiente de conexiones.
 */
public class WebServer {
    private static final int PORT = 7770;
    private static final int MAX_THREADS = 5; // Maxima cantidad de hilos
    private static ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection accepted: " + clientSocket.getInetAddress());

                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }
}
