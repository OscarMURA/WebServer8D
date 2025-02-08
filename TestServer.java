import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Pruebas para el servidor web con soporte multi-hilos y concurrencia.
 */
public class TestServer {

    private static final String SERVER_URL = "http://127.0.0.1:7770";
    private static final int MAX_THREADS = 5; 

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

    /**
     * Prueba que el servidor responde con 200 OK cuando se solicita un archivo existente.
     */
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

    /**
     * Prueba que el servidor responde con 404 Not Found cuando se solicita un archivo inexistente.
     */
    @Test
    void testResponse404() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/no-existe.html").openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        assertEquals(404, responseCode, "El servidor debe responder con 404 Not Found");
    }

    /**
     * Prueba que el servidor maneja múltiples solicitudes simultáneas con ThreadPool.
     * Se hacen 5 solicitudes concurrentes (máximo de hilos en el pool).
     */
    @Test
    void testMultipleConcurrentRequests() throws Exception {
        int numRequests = MAX_THREADS; 
        ExecutorService executor = Executors.newFixedThreadPool(numRequests);
        
        Future<Integer>[] responses = new Future[numRequests];

        for (int i = 0; i < numRequests; i++) {
            responses[i] = executor.submit(() -> {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/index.html").openConnection();
                    connection.setRequestMethod("GET");
                    return connection.getResponseCode();
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            });
        }

        for (Future<Integer> response : responses) {
            assertEquals(200, response.get(), "Todas las solicitudes deben recibir 200 OK");
        }

        executor.shutdown();
    }

    /**
     * Prueba cómo el servidor maneja una carga mayor a su capacidad de hilos.
     * Se envían 8 solicitudes cuando el ThreadPool solo permite 5 concurrentes.
     */
    @Test
    void testOverloadedServerBeyondThreadPoolLimit() throws Exception {
        int numRequests = MAX_THREADS + 3; 
        ExecutorService executor = Executors.newFixedThreadPool(numRequests);
        
        Future<Integer>[] responses = new Future[numRequests];

        for (int i = 0; i < numRequests; i++) {
            responses[i] = executor.submit(() -> {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/index.html").openConnection();
                    connection.setRequestMethod("GET");
                    return connection.getResponseCode();
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            });
        }

        int successfulResponses = 0;
        int failedResponses = 0;

        for (Future<Integer> response : responses) {
            int statusCode = response.get();
            if (statusCode == 200) {
                successfulResponses++;
            } else {
                failedResponses++;
            }
        }

        System.out.println("Solicitudes exitosas: " + successfulResponses);
        System.out.println("Solicitudes fallidas: " + failedResponses);

        assertTrue(successfulResponses >= MAX_THREADS, "Debe haber al menos " + MAX_THREADS + " respuestas exitosas");
        assertTrue(failedResponses >= 0, "Algunas solicitudes pueden fallar debido a la sobrecarga del servidor");

        executor.shutdown();
    }
}
