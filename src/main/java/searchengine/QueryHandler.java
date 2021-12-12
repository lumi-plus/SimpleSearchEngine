package searchengine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryHandler {
    private List<WebPage> searchResults;

    public QueryHandler(String fullQuery, InvertedIndex invertedIndex) {
        String[] queries = fullQuery.split("%20OR%20");
        for (String query : queries) {
            List<Set<WebPage>> responses = new ArrayList<>();
            Set<WebPage> allResponses = new HashSet<>();
            String[] searchTerms = query.split("%20");
            for (String searchTerm : searchTerms) {
                Set<WebPage> response = new HashSet<>();
                searchTerm = searchTerm.toLowerCase();
                for (WebPage page : invertedIndex.getPages(searchTerm)) {
                    response.add(page);
                    allResponses.add(page);
                }
                responses.add(response);
            }
            searchResults = new ArrayList<>(allResponses);
            for (Set<WebPage> response : responses) {
                for (WebPage page : allResponses) {
                    if (!response.contains(page)) {
                        searchResults.remove(page);
                    }
                }
            }
        }
    }

    public List<String> getSearchResponse() {
        List<String> response = new ArrayList<>();
        for (WebPage page : getSearchResults()) {
            response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
                    page.getUrl(), page.getTitle()));
        }
        return response;
    }

    public List<WebPage> getSearchResults() {
        return searchResults;
    }
}
