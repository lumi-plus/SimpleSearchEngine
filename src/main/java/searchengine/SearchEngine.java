package searchengine;

import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;

public class SearchEngine {
    private WebServer server;
    private FileReader fileReader;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public SearchEngine(WebServer server) {
        this.server = server;
    }

    public List<WebPage> searchPages(String searchTerm, FileReader fileReader) {
        this.fileReader = fileReader;
        var result = new ArrayList<WebPage>();
        for (var page : fileReader.getPages()) {
            if (page.getContent().contains(searchTerm)) {
                result.add(page);
            }
        }
        return result;
    }

    public void search(HttpExchange io) {
        var searchTerm = io.getRequestURI().getRawQuery().split("=")[1];
        var response = new ArrayList<String>();
        for (var page : searchPages(searchTerm, fileReader)) {
            response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
                    page.getUrl(), page.getTitle()));
        }
        var bytes = response.toString().getBytes(CHARSET);
        server.respond(io, 200, "application/json", bytes);
    }
}
