package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class TFIDF extends TF {
    private InvertedIndex invertedIndex;

    public TFIDF(InvertedIndex invertedIndex) {
        super(invertedIndex);
        this.invertedIndex = invertedIndex;
    }

    @Override
    public List<WebPage> rank(Set<WebPage> pages, String fullQuery) {
        String[] queries = fullQuery.split("%20OR%20");
        Map<WebPage, Double> rankings = new HashMap<>();
        for (String query : queries) {
            query = query.toLowerCase();
            for (WebPage page : pages) {
                double score = computeFrequency(query, page, pages);
                if (Double.isNaN(score)) {
                    break;
                }
                if (rankings.containsKey(page)) {
                    score = Math.max(rankings.get(page), score);
                }
                // System.out.println(page.getTitle()+": "+score);
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

    // public double termFrequency(String term, List<String> document) {
    //     double count = 0;
    //     for (String query : term.split("%20")) {
    //         if (invertedIndex.getPages(query).isEmpty()) {
    //             return 0;
    //         }
    //         for (String word : document) {
    //             if (word.equals(query)) {
    //                 count++;
    //             }
    //         }
    //     }
        // List<String> terms = new ArrayList<>();
        // Collections.addAll(terms, term.split("%20"));
        // for (String word : document) {
        // if (terms.contains(word)) {
        // count++;
        // }
        // }
    //     return count / document.size();
    // }

    public double inverseDocumentFrequency(String term, Set<WebPage> documents) {
        List<Set<WebPage>> allResponses = new ArrayList<>();
        Set<WebPage> pages = new HashSet<>();
        for (String query : term.split("%20")) {
            var response = invertedIndex.getPages(query);
            allResponses.add(response);
            for (WebPage page : response) {
                pages.add(page);
            }
        }
        List<WebPage> result = new ArrayList<>(pages);
        for (Set<WebPage> r1 : allResponses) {
            for (WebPage page : r1) {
                for (Set<WebPage> r2 : allResponses) {
                    if (!r2.contains(page)) {
                        result.remove(page);
                    }
                }
            }
        }
        return Math.log10((double) documents.size() / result.size() +1);
    }

    @Override
    public double computeFrequency(String term, WebPage document, Set<WebPage> documents) {
        double tf = super.termFrequency(term, document.getContent());
        double idf = inverseDocumentFrequency(term, documents);
        // System.out.println(document.getTitle() + ", tfidf: " + tf*idf);
        return tf * idf;
    }

    // @Override
    // public double termFrequency(String term, List<String> document) {
    //     // TODO Auto-generated method stub
    //     return super.termFrequency(term, document);
    // }

}
