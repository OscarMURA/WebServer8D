import java.io.*;
import java.net.*;

/**
 * Esta clase maneja cada cliente en un hilo separado.
 */
public class ClientHandler implements Runnable {
    
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            handleRequest();
        } catch (Exception e) {
            System.err.println("Error handling client request: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private void handleRequest() throws IOException {
        
    }
}
