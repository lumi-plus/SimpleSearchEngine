package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class TFIDF {
    private InvertedIndex invertedIndex;
    public TFIDF(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    public List<WebPage> rank(Set<WebPage> pages, String fullQuery) {
        String[] queries = fullQuery.split("%20OR%20");
        Map<WebPage, Double> rankings = new HashMap<>();
        for (String query : queries) {
            query = query.toLowerCase();
            for (WebPage page : pages) {
                double score = computeTFID(query, page, pages);
                if (rankings.containsKey(page)) {
                    score = Math.max(rankings.get(page), score);
                }
                rankings.put(page, score);
            }
        }
        return sortRanking(rankings);
    }

    public List<WebPage> sortRanking(Map<WebPage, Double> map) {
        Map<WebPage, Double> sortedMap = map.entrySet().stream()
                .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                        (e2, e1) -> e2, LinkedHashMap::new));
        return new ArrayList<>(sortedMap.keySet());
    }

    public double termFrequency(String term, List<String> document) {
        double count = 0;
        List<String> terms = new ArrayList<>();
        Collections.addAll(terms, term.split("%20"));
        for (String word : document) {
            if (terms.contains(word)) {
                count++;
            }
        }
        double score = count / document.size();
        return score;
    }

    public double inverseDocumentFrequency(String term, Set<WebPage> documents) {
        List<WebPage> pages = invertedIndex.getPages(term);
        double score = Math.log(documents.size() / pages.size());
        return score;
    }

    public double computeTFID(String term, WebPage document, Set<WebPage> documents) {
        // System.out.println("term: " + term);
        double tf = termFrequency(term, document.getContent());
        double idf = inverseDocumentFrequency(term, documents);
        return tf * idf;
    }
}
