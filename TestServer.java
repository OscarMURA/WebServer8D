

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Pruebas para el servidor web.
 */
public class TestServer {

    private static final String SERVER_URL = "http://127.0.0.1:7770";

    @BeforeAll
    static void startServer() throws Exception {
        new Thread(() -> {
            try {
                WebServer.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(2000);
    }

    @Test
    void testResponse200() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/index.html").openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        assertEquals(200, responseCode, "El servidor debe responder con 200 OK");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        assertTrue(response.toString().contains("<html>"), "Debe contener una estructura HTML válida");
    }

    @Test
    void testResponse404() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/no-existe.html").openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        assertEquals(404, responseCode, "El servidor debe responder con 404 Not Found");
    }

}
