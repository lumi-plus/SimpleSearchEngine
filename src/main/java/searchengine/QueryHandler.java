package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryHandler {
    private InvertedIndex invertedIndex;
    private TFIDF tfidf;

    public QueryHandler(InvertedIndex invertedIndex, TFIDF tfidf) {
        this.invertedIndex = invertedIndex;
        this.tfidf = tfidf;
    }

    public Set<WebPage> getSearchResults(String fullQuery) {
        String[] queries = fullQuery.split("%20OR%20");
        Set<WebPage> searchResults = new HashSet<>();
        for (String query : queries) {
            Set<List<WebPage>> responses = new HashSet<>();
            Set<WebPage> allResponses = new HashSet<>();
            String[] searchTerms = query.split("%20");
            for (String searchTerm : searchTerms) {
                List<WebPage> response = new ArrayList<>();
                searchTerm = searchTerm.toLowerCase();
                System.out.println("search term: "+searchTerm);
                for (WebPage page : invertedIndex.getPages(searchTerm)) {
                    response.add(page);
                    allResponses.add(page);
                }
                responses.add(response);
            }
            Set<WebPage> tmp = new HashSet<>(allResponses);
            for (List<WebPage> response : responses) {
                for (WebPage page : allResponses) {
                    if (!response.contains(page)) {
                        tmp.remove(page);
                    }
                }
            }
            Collections.addAll(searchResults, tmp.toArray(new WebPage[0]));
        }
        return searchResults;
    }
}
