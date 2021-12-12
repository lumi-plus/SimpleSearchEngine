package searchengine;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class SearchEngine {
    private WebServer server;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public SearchEngine(WebServer server) {
        this.server = server;
    }

    public void search(HttpExchange io, InvertedIndex invertedIndex) {
        String query = io.getRequestURI().getRawQuery().split("=")[1];
        QueryHandler queryHandler = new QueryHandler(query, invertedIndex);
        List<WebPage> searchResults = queryHandler.getSearchResults();
        TFIDF tfidf = new TFIDF(searchResults, query);
        List<WebPage> rankedResults = tfidf.getRankedResults();
        // Map<WebPage, Double> rankedResults = tfidf.getRankedResults();
        // List<String> searchResponse = getSearchResponse(searchResults);
        List<String> searchResponse = getSearchResponse(rankedResults);
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

    // public List<String> getSearchResponse(List<WebPage> pages) {
    //     List<String> response = new ArrayList<>();
    //     for (WebPage page : pages) {
    //         response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
    //                 page.getUrl(), page.getTitle()));
    //     }
    //     return response;
    // }

}
