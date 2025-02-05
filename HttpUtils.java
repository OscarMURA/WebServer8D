
import java.io.*;

/**
 * Clase de utilidades para manejar respuestas HTTP.
 */
public class HttpUtils {

    private static final String CRLF = "\r\n"; // Fin de línea en HTTP

    
    /**
     * Sends an HTTP response to the client.
     *
     * @param out           the DataOutputStream to write the response to
     * @param statusCode    the HTTP status code (e.g., 200 for OK, 404 for Not Found)
     * @param statusMessage the HTTP status message (e.g., "OK", "Not Found")
     * @param responseBody  the body of the HTTP response
     * @throws IOException if an I/O error occurs while writing the response
     */
    public static void sendHttpResponse(DataOutputStream out, int statusCode, String statusMessage, String responseBody) throws IOException {
        String responseHeader = "HTTP/1.1 " + statusCode + " " + statusMessage + CRLF +
                                "Content-Type: text/html" + CRLF +
                                "Content-Length: " + responseBody.length() + CRLF +
                                CRLF;

        out.writeBytes(responseHeader);
        out.writeBytes(responseBody);
        out.flush();
    }

    
    /**
     * Sends a 404 Not Found HTTP response to the client.
     *
     * @param out the DataOutputStream to send the response through
     * @throws IOException if an I/O error occurs while sending the response
     */
    public static void sendHttpResponse404(DataOutputStream out) throws IOException {
        String responseBody = "<html><body><h1>404 Not Found</h1></body></html>";
        sendHttpResponse(out, 404, "Not Found", responseBody);
    }

    
    
    /**
     * Sends an HTTP 200 OK response with the contents of the specified file.
     *
     * @param out  the DataOutputStream to send the response to
     * @param file the File to be sent in the response body
     * @throws IOException if an I/O error occurs while reading the file or writing the response
     */
    public static void sendHttpResponse200(DataOutputStream out, File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        int fileLength = (int) file.length();
        byte[] fileData = new byte[fileLength];
        fis.read(fileData);
        fis.close();
    
        System.out.println("Sending 200 OK for: " + file.getName());
    
        out.writeBytes("HTTP/1.1 200 OK" + CRLF);
        out.writeBytes("Content-Type: " + getMimeType(file.getName()) + CRLF);
        out.writeBytes("Content-Length: " + fileLength + CRLF);
        out.writeBytes(CRLF); // Línea en blanco obligatoria
    
        out.write(fileData, 0, fileLength);
        out.flush();
    }
    

    
    
    /**
     * Returns the MIME type of a file based on its extension.
     *
     * @param fileName the name of the file whose MIME type is to be determined
     * @return the MIME type as a string. Possible values include:
     *         "text/html" for .htm and .html files,
     *         "image/jpeg" for .jpg and .jpeg files,
     *         "image/gif" for .gif files,
     *         "image/png" for .png files,
     *         "text/css" for .css files,
     *         "application/javascript" for .js files,
     *         "application/octet-stream" for all other file types.
     */
    private static String getMimeType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (fileName.endsWith(".gif")) {
            return "image/gif";
        }
        if (fileName.endsWith(".png")) {
            return "image/png";
        }
        if (fileName.endsWith(".css")) {
            return "text/css";
        }
        if (fileName.endsWith(".js")) {
            return "application/javascript";
        }
        return "application/octet-stream";
    }

    
    
    /**
     * Sends the bytes from a FileInputStream to an OutputStream.
     *
     * @param fis the FileInputStream to read from
     * @param os the OutputStream to write to
     * @throws IOException if an I/O error occurs
     */
    public static void enviarBytes(FileInputStream fis, OutputStream os) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
    }
}
