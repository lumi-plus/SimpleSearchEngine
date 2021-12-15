package searchengine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
    private static final int PORT = 8080;
    private static final int BACKLOG = 0;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private HttpServer httpServer;

    public WebServer(int port, String filename) throws IOException {
        System.out.println("filename: " + filename);
        FileReader fileReader = new FileReader(filename);
        InvertedIndex invertedIndex = new InvertedIndex(fileReader.getPages());
        QueryHandler queryHandler = new QueryHandler(invertedIndex);
        RankingAlgorithm rankAlgoritm = new TFIDF(invertedIndex);
        SearchEngine searchEngine = new SearchEngine(this, queryHandler, rankAlgoritm);
        httpServer = HttpServer.create(new InetSocketAddress(port), BACKLOG);
        httpServer.createContext("/", io -> respond(io, 200, "text/html", fileReader.getFile("web/index.html")));
        httpServer.createContext("/search", searchEngine::search);
        httpServer.createContext(
                "/favicon.ico", io -> respond(io, 200, "image/x-icon", fileReader.getFile("web/favicon.ico")));
        httpServer.createContext(
                "/code.js", io -> respond(io, 200, "application/javascript", fileReader.getFile("web/code.js")));
        httpServer.createContext(
                "/style.css", io -> respond(io, 200, "text/css", fileReader.getFile("web/style.css")));
        httpServer.start();
        String msg = " WebServer running on http://localhost:" + port + " ";
        System.out.println("╭" + "─".repeat(msg.length()) + "╮");
        System.out.println("│" + msg + "│");
        System.out.println("╰" + "─".repeat(msg.length()) + "╯");
    }

    public void respond(HttpExchange io, int code, String mime, byte[] response) {
        try {
            io.getResponseHeaders()
                    .set("Content-Type", String.format("%s; charset=%s", mime, CHARSET.name()));
            io.sendResponseHeaders(200, response.length);
            io.getResponseBody().write(response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            io.close();
        }
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public static void main(final String... args) throws IOException {
        String filename = Files.readString(Paths.get("config.txt")).strip();
        new WebServer(PORT, filename);
    }
}