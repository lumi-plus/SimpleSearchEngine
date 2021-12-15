package searchengine;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;

public class SearchEngine {
    private WebServer server;
    private QueryHandler queryHandler;
    private RankAlgoritm rankAlgoritm;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public SearchEngine(WebServer server, QueryHandler queryHandler,
            RankAlgoritm rankAlgoritm) {
        this.server = server;
        this.queryHandler = queryHandler;
        this.rankAlgoritm = rankAlgoritm;
    }

    public void search(HttpExchange io) {
        String query = io.getRequestURI().getRawQuery().split("=")[1];
        Set<WebPage> searchResults = queryHandler.getSearchResults(query);
        List<WebPage> rankedResults = rankAlgoritm.rank(searchResults, query);
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
