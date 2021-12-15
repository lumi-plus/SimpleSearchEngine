package searchengine;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class RankingAlgorithm {
    public abstract Map<WebPage, Double> rank(Set<WebPage> pages, String fullQuery);
    public abstract List<WebPage> sortRanking(Map<WebPage, Double> map);
    public abstract double computeRank(String term, WebPage document, Set<WebPage> documents) ;
}
