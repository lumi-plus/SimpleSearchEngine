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
        // ArrayList<WebPage> result = new ArrayList<>();
        // for (WebPage page : fileReader.getPages()) {
        //     if (page.getContent().contains(searchTerm)) {
        //         result.add(page);
        //     }
        // }
        // return result;
        return ii.getPages(searchTerm);
    }

    public void search(HttpExchange io) {
        String query = io.getRequestURI().getRawQuery().split("=")[1];
        Set<String> response = new HashSet<String>();
        Set<String> searchTerms = new HashSet<>();
        Collections.addAll(searchTerms, query.split("%20OR%20"));
        for(String searchTerm : searchTerms) {
            for (WebPage page : fetchPages(searchTerm)) {
                response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
                    page.getUrl(), page.getTitle()));
            }
        }
        byte[] bytes = response.toString().getBytes(CHARSET);
        server.respond(io, 200, "application/json", bytes);
    }

}
