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

public class TF extends RankingAlgorithm {
    private InvertedIndex invertedIndex;

    public TF(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

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
                // System.out.println(page.getTitle()+": "+score);
                rankings.put(page, score);
            }
        }
        return rankings;
    }

    @Override
    public List<WebPage> sortRanking(Map<WebPage, Double> map) {
        Map<WebPage, Double> sortedMap = map.entrySet().stream()
                .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                        (e2, e1) -> e2, LinkedHashMap::new));
        return new ArrayList<>(sortedMap.keySet());
    }

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

    @Override
    public double computeRank(String term, WebPage document, Set<WebPage> documents) {
        return termFrequency(term, document.getContent());
    }

}
