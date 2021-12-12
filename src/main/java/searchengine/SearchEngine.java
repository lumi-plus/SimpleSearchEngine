package searchengine;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

public class SearchEngine {
    private WebServer server;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public SearchEngine(WebServer server) {
        this.server = server;
    }

    public void search(HttpExchange io, InvertedIndex invertedIndex) {
        String fullQuery = io.getRequestURI().getRawQuery().split("=")[1];
        QueryHandler queryHandler = new QueryHandler(fullQuery, invertedIndex);
        List<WebPage> searchResults = queryHandler.getSearchResults();
        List<String> searchResponse = getSearchResponse(searchResults);
        byte[] bytes = searchResponse.toString().getBytes(CHARSET);
        server.respond(io, 200, "application/json", bytes);
    }

    public List<String> getSearchResponse(List<WebPage> pages) {
        List<String> response = new ArrayList<>();
        for (WebPage page : pages) {
            response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
                    page.getUrl(), page.getTitle()));
        }
        return response;
    }

}
