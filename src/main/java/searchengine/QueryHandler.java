package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryHandler {
    private InvertedIndex invertedIndex;

    public QueryHandler(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    public Set<WebPage> getSearchResults(String fullQuery) {
        String[] queries = fullQuery.split("%20OR%20");
        Set<WebPage> searchResults = new HashSet<>();
        for (String query : queries) {
            List<List<WebPage>> responses = new ArrayList<>();
            Set<WebPage> allResponses = new HashSet<>();
            String[] searchTerms = query.split("%20");
            for (String searchTerm : searchTerms) {
                List<WebPage> response = new ArrayList<>();
                searchTerm = searchTerm.toLowerCase();
                for (WebPage page : invertedIndex.getPages(searchTerm)) {
                    response.add(page);
                    allResponses.add(page);
                }
                responses.add(response);
            }
            List<WebPage> result = new ArrayList<>(allResponses);
            for (List<WebPage> r1 : responses) {
                for (WebPage page : r1) {
                    for (List<WebPage> r2: responses) {
                        if (!r2.contains(page)) {
                            result.remove(page);
                        }
                    }
                }
            }
            Collections.addAll(searchResults, result.toArray(new WebPage[0]));
        }
        System.out.println("size: "+searchResults.size());
        return searchResults;
    }
}
