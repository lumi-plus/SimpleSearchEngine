
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
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class WebServerTest {
    private WebServer webServer = null;
    // private WebServer faultyServer = null;

    @BeforeAll
    public void setUp() {
        try {
            var rnd = new Random();
            while (webServer == null) {
                try {
                    webServer = new WebServer(rnd.nextInt(60000) + 1024, "data/test-file.txt");
                    // faultyServer = new WebServer(rnd.nextInt(60000) + 1024,
                    // "data/test-file-errors.txt");
                } catch (BindException e) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public void tearDown() {
        var a = webServer.getHttpServer();
        a.stop(10);
        // faultyServer.getServer().stop(0);
        // faultyServer = null;
        webServer = null;
    }

    <<<<<<<HEAD

    @Test
    public void lookupFaultyServer() {
        String baseURL = String.format("http://localhost:%d/search?q=",
                faultyServer.getServer().getAddress().getPort());

        assertEquals("[]",
                httpGet(baseURL + " "));
    }

    @Test
    public void lookupWebServer() {
        String baseURL = String.format("http://localhost:%d/search?q=", server.getServer().getAddress().getPort());

        assertEquals(
                "[{\"url\": \"http://page1.com\", \"title\": \"title1\"}, {\"url\": \"http://page2.com\", \"title\": \"title2\"}]",
                httpGet(baseURL + "word1"));
=======
    // @Test
    // public void lookupFaultyServer() {
    //     String baseURL = String.format("http://localhost:%d/search?q=", faultyServer.getServer().getAddress().getPort());
        
    //     assertEquals("[]", 
    //         httpGet(baseURL + " "));
    // }

    @Test
    public void lookupWebServer() {
        String baseURL = String.format("http://localhost:%d/search?q=", webServer.getHttpServer().getAddress().getPort());
        
        assertEquals("[{\"url\": \"http://page1.com\", \"title\": \"title1\"}, {\"url\": \"http://page2.com\", \"title\": \"title2\"}]", 
            httpGet(baseURL + "word1"));
>>>>>>> 435b8829b0f5899ad8ffc378fab918c5af62817c

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
