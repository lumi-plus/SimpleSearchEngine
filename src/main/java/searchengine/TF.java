package searchengine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * class to calculate the term frequency of a query
 * @author skje, lmig, mers, davv
 * @version 2021.12.15
 */
public class TF extends RankingAlgorithm {
    //the inverted index maps content to a given word
    private InvertedIndex invertedIndex;

    /**
     * constructor for term frequency
     * @param invertedIndex maps content to a given word
     */
    public TF(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }
    
    /**
     * maps the score calculated in computeFrequency to a web page
     * @param pages set containing all web pages consisting of url, title and content
     * @param fullQuery query as it was entered in the search engine including "OR" and capital letters
     * @return the score mapped to each web page
     */
    @Override
    public Map<WebPage, Double> rank(Set<WebPage> pages, String fullQuery) {
        String[] queries = fullQuery.split("%20OR%20");
        Map<WebPage, Double> rankings = new HashMap<>();
        for (String query : queries) {
            query = query.toLowerCase();
            for (WebPage page : pages) {
                double score = computeRank(query, page, pages);
                if (Double.isNaN(score)) {
                    break;
                }
                if (rankings.containsKey(page)) {
                    score = Math.max(rankings.get(page), score);
                }
                rankings.put(page, score);
            }
        }
        return rankings;
    }

    /**
     * sorts the web pages based on their score in descending order
     * @param map web page mapped to its score
     * @return ArrayList with web pages sorted by their score
     */
    @Override
    public List<WebPage> sortRanking(Map<WebPage, Double> map) {
        Map<WebPage, Double> sortedMap = map.entrySet().stream()
                .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                        (e2, e1) -> e2, LinkedHashMap::new));
        return new ArrayList<>(sortedMap.keySet());
    }

    /**
     * calculates the term frequency by dividing the amount of times the term occurs in a document 
     * and the amount of words in the document
     * @param term query for which to calculate the frequency
     * @param document list of strings representing the file in which the search term occurs
     * @return the term frequency
     */
    public double termFrequency(String term, List<String> document) {
        double count = 0;
        for (String query : term.split("%20")) {
            if (invertedIndex.getPages(query).isEmpty()) {
                return 0;
            }
            for (String word : document) {
                if (word.equals(query)) {
                    count++;
                }
            }
        }
        return count / document.size();
    }

    /**
     * returns the term frequency used to rank the search results
     * @param term query for which to calculate the frequency
     * @param document list of strings representing the file in which the search term occurs
     * @param documents set of web pages in which the search term of the query occurs
     * @return term frequency
     */
    @Override
    public double computeRank(String term, WebPage document, Set<WebPage> documents) {
        return termFrequency(term, document.getContent());
    }

}
