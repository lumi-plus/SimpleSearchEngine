package searchengine;

import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * abstract class used for the implementation of different ranking algorithms for the queries, 
 * so that the ranking algorithm easily can be replaced
 * @author skje, lmig, mers, davv
 * @verison 2021.12.15
 */
public abstract class RankAlgoritm {
    /**
     * calculates a rank for the web pages found when searching for a term
     * @param pages set containing all web pages consisting of url, title and content
     * @param fullQuery query as it was entered in the search engine including "OR" and capital letters
     * @return list of WebPage with a score calculated in the class extending the abstract class
     */
    public abstract List<WebPage> rank(Set<WebPage> pages, String fullQuery);
    /**
     * sorts search results based on their score
     * @param map a map consisting of the web pages mapped to their score
     * @return list of WebPage sorted in the order of the score provided by the class extending the abstract class
     */
    public abstract List<WebPage> sortRanking(Map<WebPage, Double> map);
}
