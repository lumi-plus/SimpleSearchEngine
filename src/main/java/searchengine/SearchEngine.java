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
    private RankingAlgorithm rankingAlgorithm;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public SearchEngine(WebServer server, QueryHandler queryHandler,
            RankingAlgorithm rankAlgoritm) {
        this.server = server;
        this.queryHandler = queryHandler;
        this.rankingAlgorithm = rankAlgoritm;
    }

    public void search(HttpExchange io) {
        String query = io.getRequestURI().getRawQuery().split("=")[1];
        Set<WebPage> searchResults = queryHandler.getSearchResults(query);
        Map<WebPage, Double> rankedMap = rankingAlgorithm.rank(searchResults, query);
        List<WebPage> rankedResults = rankingAlgorithm.sortRanking(rankedMap);
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
