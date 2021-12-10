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
    private FileReader fileReader;
    private InvertedIndex ii; 
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public SearchEngine(WebServer server, FileReader fileReader, InvertedIndex ii) {
        this.server = server;
        this.fileReader = fileReader;
        this.ii = ii;
    }

    public List<WebPage> fetchPages(String searchTerm) {
        // List<WebPage> result = new ArrayList<>();
        // for (WebPage page : fileReader.getPages()) {
        //     if (page.getContent().contains(searchTerm)) {
        //         result.add(page);
        //     }
        // }
        // return result;
        return ii.getPages(searchTerm);
    }

    public void search(HttpExchange io) {
        String fullQuery = io.getRequestURI().getRawQuery().split("=")[1];
        Set<String> finalResponse = new HashSet<>();
        Set<String> queries = new HashSet<>();
        Collections.addAll(queries, fullQuery.split("%20OR%20")); 
        for (String query : queries) {
            List<Set<String>> responses = new ArrayList<>();
            Set<String> searchTerms = new HashSet<>();
            Set<String> allResponses = new HashSet<>();
            // query = query.replace("%20", " ");
            Collections.addAll(searchTerms, query.split("%20"));
            // Collections.addAll(searchTerms, query.split(" OR "));
            for(String searchTerm : searchTerms) {
                Set<String> response = new HashSet<>();
                searchTerm = searchTerm.toLowerCase();
                System.out.println("query: "+searchTerm);
                for (WebPage page : fetchPages(searchTerm)) {
                    response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
                        page.getUrl(), page.getTitle()));
                        allResponses.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
                        page.getUrl(), page.getTitle()));
                }
                responses.add(response);
                
            }
            Set<String> response = new HashSet<>(allResponses);
            for (Set<String> r1 : responses){
                for (String webpage : allResponses){
                    if (!r1.contains(webpage)){
                        response.remove(webpage);
                    }
                }
            }
            // String[] a = response.toArray(new String[0]);
            Collections.addAll(finalResponse, response.toArray(new String[0]));
        }
        byte[] bytes = finalResponse.toString().getBytes(CHARSET);
        server.respond(io, 200, "application/json", bytes);
    }

}
