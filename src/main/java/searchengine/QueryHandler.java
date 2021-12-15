package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * class responsible for converting searches into refined queries
 * @author skje, lmig, mers, davv
 * @version 2021.12.15
 */
public class QueryHandler {
    //maps content to a given word
    private InvertedIndex invertedIndex;

    /**
     * creates a query handler using an inverted index 
     * @param invertedIndex maps content to a given word
     */
    public QueryHandler(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    /**
     * looks through the inverted index for pages containing a given search term or search terms, if the "OR" separator is use,
     * and adds the results to a set
     * @param fullQuery query as it was entered in the search engine including "OR" and capital letters
     * @return a set with all search results
     */
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
        return searchResults;
    }
}
