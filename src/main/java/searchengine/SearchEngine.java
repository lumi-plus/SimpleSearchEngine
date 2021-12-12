package searchengine;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;

public class SearchEngine {
    private WebServer server;
    private InvertedIndex invertedIndex;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public SearchEngine(WebServer server, InvertedIndex ii) {
        this.server = server;
        this.invertedIndex = ii;
    }

    public List<WebPage> getPages(String searchTerm) {
        return invertedIndex.getPages(searchTerm);
    }

    public void search(HttpExchange io) {
        String fullQuery = io.getRequestURI().getRawQuery().split("=")[1];
        String[] queries = fullQuery.split("%20OR%20");
        List<String> searchResults = new ArrayList<>();
        for (String query : queries) {
            List<Set<String>> responses = new ArrayList<>();
            Set<String> allResponses = new HashSet<>();
            String[] searchTerms = query.split("%20");
            for (String searchTerm : searchTerms) {
                Set<String> response = new HashSet<>();
                searchTerm = searchTerm.toLowerCase();
                for (WebPage page : getPages(searchTerm)) {
                    response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
                            page.getUrl(), page.getTitle()));
                    allResponses.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
                            page.getUrl(), page.getTitle()));
                }
                responses.add(response);
            }
            List<String> tmp = new ArrayList<>(allResponses);
            for (Set<String> response : responses) {
                for (String webpage : allResponses) {
                    if (!response.contains(webpage)) {
                        tmp.remove(webpage);
                    }
                }
            }
            Collections.addAll(searchResults, tmp.toArray(new String[0]));
        }
        byte[] bytes = searchResults.toString().getBytes(CHARSET);
        server.respond(io, 200, "application/json", bytes);
    }

}
