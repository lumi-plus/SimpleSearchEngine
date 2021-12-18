package searchengine;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;

/**
 * class is the link between the server and different search components. It
 * ties all the classes together.
 * 
 * @author skje, lmig, mers, davv
 * @version 2021.12.15
 */
public class SearchEngine {
    private WebServer server;
    // converts searches into refined queries
    private QueryHandler queryHandler;
    // sorts search results based on a score
    private RankingAlgorithm rankingAlgorithm;
    //
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * creates a SearchEngine object
     * 
     * @param server        web server
     * @param invertedIndex maps content to a given word
     * @param queryHandler  converts searches into refined queries
     * @param rankAlgoritm  sorts search results based on a score
     */
    public SearchEngine(WebServer server, QueryHandler queryHandler,
            RankingAlgorithm rankingAlgorithm) {
        this.server = server;
        this.queryHandler = queryHandler;
        this.rankingAlgorithm = rankingAlgorithm;
    }

    /**
     * creates a connection between the server and the Java code to search for a
     * given term
     * responsible for giving the server the requested result of a query as a byte
     * array,
     * the server then responds to this request
     * 
     * @param io HttpExchange
     */
    public void search(HttpExchange io) {
        String query = io.getRequestURI().getRawQuery().split("=")[1];
        Set<WebPage> searchResults = queryHandler.getSearchResults(query);
        Map<WebPage, Double> rankedMap = rankingAlgorithm.rank(searchResults, query);
        List<WebPage> rankedResults = rankingAlgorithm.sortRanking(rankedMap);
        List<String> searchResponse = getSearchResponse(rankedResults);
        byte[] bytes = searchResponse.toString().getBytes(CHARSET);
        server.respond(io, 200, "application/json", bytes);
    }

    /**
     * creates a list of strings consisting of the url and title of the web pages
     * 
     * @param pages contains all web pages consisting of url, title and content
     * @return list of strings consisting of the url and title
     */
    public List<String> getSearchResponse(List<WebPage> pages) {
        List<String> response = new ArrayList<>();
        for (WebPage page : pages) {
            response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
                    page.getUrl(), page.getTitle()));
        }
        return response;
    }
}
