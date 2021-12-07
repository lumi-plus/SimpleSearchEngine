package searchengine;

import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;

public class SearchEngine {
    private WebServer server;
    private FileReader fileReader;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public SearchEngine(WebServer server, FileReader fileReader) {
        this.server = server;
        this.fileReader = fileReader;
    }

    public List<WebPage> fetchPages(String searchTerm) {
        var result = new ArrayList<WebPage>();
        System.out.println("fileReader.getPages()size(): "+fileReader.getPages().size());
        for (var page : fileReader.getPages()) {
            if (page.getContent().contains(searchTerm)) {
                result.add(page);
            }
        }
        return result;
    }

    public void search(HttpExchange io) {
        var query = io.getRequestURI().getRawQuery().split("=")[1];
        var response = new ArrayList<String>();
        String[] searchTerms = query.split("OR");
        for(String searchTerm : searchTerms){
            for (var page : fetchPages(searchTerm)) {
                response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
                    page.getUrl(), page.getTitle()));
            }
        }
        var bytes = response.toString().getBytes(CHARSET);
        server.respond(io, 200, "application/json", bytes);
    }

}
