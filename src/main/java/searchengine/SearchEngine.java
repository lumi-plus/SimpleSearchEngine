package searchengine;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.management.Query;

import com.sun.net.httpserver.HttpExchange;

public class SearchEngine {
    private WebServer server;
    private InvertedIndex invertedIndex;
    private QueryHandler queryHandler;
    private TFIDF tfidf;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public SearchEngine(WebServer server, InvertedIndex invertedIndex, QueryHandler queryHandler, TFIDF tfidf) {
        this.server = server;
        this.invertedIndex = invertedIndex;
        this.queryHandler = queryHandler;
        this.tfidf = tfidf;
    }

    public void search(HttpExchange io) {
        String query = io.getRequestURI().getRawQuery().split("=")[1];
        Set<WebPage> searchResults = queryHandler.getSearchResults(query);
        List<WebPage> rankedResults = tfidf.rank(searchResults, query);
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
}
