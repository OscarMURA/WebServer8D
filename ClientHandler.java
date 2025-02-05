
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Esta clase maneja cada cliente en un hilo separado.
 */
public class ClientHandler implements Runnable {

    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    /**
     * The run method is the entry point for the thread. It handles the client request
     * by invoking the handleRequest method. If an exception occurs during the handling
     * of the request, it logs an error message. Finally, it ensures that the client
     * socket is closed, logging any errors that occur during the closing process.
     */
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

    /**
     * Handles an incoming client request. This method reads the request line from the client,
     * parses the HTTP method and requested file name, and sends the appropriate HTTP response.
     * 
     * @throws IOException if an I/O error occurs while reading from or writing to the client socket.
     * 
     * The method performs the following steps:
     * 1. Reads the request line from the client.
     * 2. Checks if the request line is null and returns if it is.
     * 3. Parses the HTTP method and requested file name from the request line.
     * 4. If the method is not "GET", sends a 501 Not Implemented response.
     * 5. Constructs the file path and checks if the file exists and is not a directory.
     * 6. If the file does not exist or is a directory, sends a 404 Not Found response.
     * 7. If the file exists, sends a 200 OK response with the file content.
     */
    private void handleRequest() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

        String requestLine = in.readLine();
        if (requestLine == null) return;
        System.out.println("Request: " + requestLine);

        StringTokenizer tokens = new StringTokenizer(requestLine);
        String method = tokens.nextToken();
        String fileName = tokens.nextToken();

        if (!method.equals("GET")) {
            HttpUtils.sendHttpResponse(out, 501, "Not Implemented", "Método no soportado");
            return;
        }

        fileName = "." + fileName;
        File file = new File(fileName);
        System.out.println("File requested: " + fileName);

        if (!file.exists() || file.isDirectory()) {
            HttpUtils.sendHttpResponse404(out);
            return;
        }

        HttpUtils.sendHttpResponse200(out, file);
    }
}
