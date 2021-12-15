package searchengine;

import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * abstract class providing the framework to plug different ranking algorithms into the code
 * @author skje, lmig, mers, davv
 * @version 2021.12.15
 */
public abstract class RankingAlgorithm {
    /**
     * maps the score calculated in computeFrequency to a web page
     * @param pages set containing all web pages consisting of url, title and content
     * @param fullQuery query as it was entered in the search engine including "OR" and capital letters
     * @return map with a web page and its score
     */
    public abstract Map<WebPage, Double> rank(Set<WebPage> pages, String fullQuery);

    /**
     * sorts the web pages based on their score in descending order
     * @param map web page mapped to its score
     * @return ArrayList with web pages sorted by their score
     */
    public abstract List<WebPage> sortRanking(Map<WebPage, Double> map);

    /**
     * returns the score used to rank the search results
     * @param term query for which to calculate the frequency
     * @param document list of strings representing the file in which the search term occurs
     * @param documents set of web pages in which the search term of the query occurs
     * @return the score for all web pages containing a given query
     */
    public abstract double computeRank(String term, WebPage document, Set<WebPage> documents) ;
}
