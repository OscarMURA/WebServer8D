import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

        // Leer la primera línea de la solicitud HTTP
        String requestLine = in.readLine();
        if (requestLine == null) return;
        System.out.println("Request: " + requestLine);

        StringTokenizer tokens = new StringTokenizer(requestLine);
        String method = tokens.nextToken();
        String fileName = tokens.nextToken();

        // Se asume que solo se procesarán solicitudes GET
        if (!method.equals("GET")) {
            sendErrorResponse(out, 501, "Not Implemented");
            return;
        }

        // Obtener el nombre del archivo solicitado
        fileName = "." + fileName;
        File file = new File(fileName);
        boolean fileExists = file.exists();

        FileInputStream fis = null;
        if (fileExists) {
            fis = new FileInputStream(file);
        } else {
            sendErrorResponse(out, 404, "Not Found");
            return;
        }

        // Enviar respuesta HTTP con el archivo solicitado
        sendResponse(out, fis, fileName);
        fis.close();
    }

    private void sendResponse(DataOutputStream out, FileInputStream fis, String fileName) throws IOException {
        String statusLine = "HTTP/1.0 200 OK\r\n";
        String contentTypeLine = "Content-Type: " + getContentType(fileName) + "\r\n";
        String blankLine = "\r\n";

        out.writeBytes(statusLine);
        out.writeBytes(contentTypeLine);
        out.writeBytes(blankLine);

        sendFile(fis, out);
    }

    private void sendErrorResponse(DataOutputStream out, int statusCode, String statusMessage) throws IOException {
        String statusLine = "HTTP/1.0 " + statusCode + " " + statusMessage + "\r\n";
        String contentTypeLine = "Content-Type: text/html\r\n";
        String blankLine = "\r\n";
        String body = "<html><head><title>" + statusCode + " " + statusMessage + "</title></head>" +
                      "<body><h1>" + statusCode + " " + statusMessage + "</h1></body></html>";

        out.writeBytes(statusLine);
        out.writeBytes(contentTypeLine);
        out.writeBytes(blankLine);
        out.writeBytes(body);
    }

    private void sendFile(FileInputStream fis, DataOutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }

    private String getContentType(String fileName) {
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        } else if (fileName.endsWith(".jpg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }
}
