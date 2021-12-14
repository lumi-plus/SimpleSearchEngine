package searchengine;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;

public class SearchEngine {
    private WebServer server;
    private InvertedIndex invertedIndex;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public SearchEngine(WebServer server, InvertedIndex invertedIndex) {
        this.server = server;
        this.invertedIndex = invertedIndex;
    }

    public void search(HttpExchange io) {
        String query = io.getRequestURI().getRawQuery().split("=")[1];
        TFIDF tfidf = new TFIDF(invertedIndex);
        QueryHandler queryHandler = new QueryHandler(invertedIndex, tfidf);
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
