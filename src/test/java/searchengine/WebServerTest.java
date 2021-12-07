
package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.BindException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

// @TestInstance(Lifecycle.PER_CLASS)
public class WebServerTest {
    private WebServer server = null;
    private WebServer faultyServer = null;

    @BeforeAll
    public void setUp() {
        try {
            var rnd = new Random();
            while (faultyServer == null && server == null) {
                try {
                    server = new WebServer(rnd.nextInt(60000) + 1024, "data/test-file.txt");
                    faultyServer = new WebServer(rnd.nextInt(60000) + 1024, "data/test-file-errors.txt");
                } catch (BindException e) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public void tearDown() {
        server.server.stop(0);
        faultyServer.server.stop(0);
        faultyServer = null;
        server = null;
    }

    @Test
    public void lookupFaultyServer() {
        String baseURL = String.format("http://localhost:%d/search?q=", faultyServer.server.getAddress().getPort());
        
        assertEquals("[]", 
            httpGet(baseURL + " "));
    }

    @Test
    public void lookupWebServer() {
        String baseURL = String.format("http://localhost:%d/search?q=", server.server.getAddress().getPort());
        
        assertEquals("[{\"url\": \"http://page1.com\", \"title\": \"title1\"}, {\"url\": \"http://page2.com\", \"title\": \"title2\"}]", 
            httpGet(baseURL + "word1"));

        assertEquals("[{\"url\": \"http://page1.com\", \"title\": \"title1\"}]",
            httpGet(baseURL + "word2"));

        assertEquals("[{\"url\": \"http://page2.com\", \"title\": \"title2\"}]", 
            httpGet(baseURL + "word3"));
            
        assertEquals("[]", 
            httpGet(baseURL + "word4"));
    }

    private String httpGet(String url) {
        var uri = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            return client.send(request, BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
