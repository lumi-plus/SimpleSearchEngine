package searchengine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * inverted index used for mapping content to a given word
 * @author skje, lmig, mers, davv
 * @version 2021.12.15
 */
public class InvertedIndex {
    //map for storing the search terms and web pages
    private Map<String, Set<WebPage>> invertedIndex;

    /**
     * creates an inverted index that maps content to a given word
     * @param pages List of WebPages consisting of url, title and content
     */
    public InvertedIndex(List<WebPage> pages) {
        invertedIndex = new HashMap<>();
        for(WebPage page : pages) {
            List<String> content = page.getContent();
            for (String word : content) {
                if (invertedIndex.containsKey(word)) {
                    invertedIndex.get(word).add(page);
                } else {
                    Set<WebPage> list = new HashSet<>();
                    list.add(page);
                    invertedIndex.put(word, list);
                }
            }
        }
    }

    /**
     * get the inverted index in the form of a set
     * @param word
     * @return Set of WebPages with content mapped to a given word
     */
    public Set<WebPage> getPages(String word) {
        if (!invertedIndex.containsKey(word)) {
            return new HashSet<>();
        }
        return invertedIndex.get(word);
    }
}
