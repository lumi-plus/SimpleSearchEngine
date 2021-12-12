package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryHandler {
    private List<String> searchResults;

    public QueryHandler(String fullQuery, InvertedIndex invertedIndex) {
        String[] queries = fullQuery.split("%20OR%20");
        searchResults = new ArrayList<>();
        for (String query : queries) {
            List<Set<String>> responses = new ArrayList<>();
            Set<String> allResponses = new HashSet<>();
            String[] searchTerms = query.split("%20");
            for (String searchTerm : searchTerms) {
                Set<String> response = new HashSet<>();
                searchTerm = searchTerm.toLowerCase();
                for (WebPage page : invertedIndex.getPages(searchTerm)) {
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
    }
    
    public List<String> getSearchResults() {
        return searchResults;
    }
}
