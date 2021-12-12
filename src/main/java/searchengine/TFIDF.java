package searchengine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TFIDF {
    private List<WebPage> rankedPages;

    public TFIDF(List<WebPage> pages, String term) {
        Map<WebPage, Double> rankMapping = new HashMap<>();
        for (WebPage page : pages) {
            rankMapping.put(page, computeTFID(term, page, pages));
        }
        rankedPages = sortRanking(rankMapping);
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
        for (String word : document) {
            if (term.equals(word)) {
                count++;
            }
        }
        return count / document.size();
    }

    public double inverseDocumentFrequency(String term, List<WebPage> documents) {
        double count = 0;
        for (WebPage document : documents) {
            for (String word : document.getContent()) {
                if (word.equals(term)) {
                    count++;
                    break;
                }
            }
        }
        return count / documents.size();
    }

    public double computeTFID(String term, WebPage document, List<WebPage> documents) {
        double tf = termFrequency(term, document.getContent());
        double idf = inverseDocumentFrequency(term, documents);
        return tf * idf;
    }

    public List<WebPage> getRankedResults() {
        return rankedPages;
    }

    // public Map<WebPage, Double> getRankedResults() {
    // return rankedPages;
    // }
}
