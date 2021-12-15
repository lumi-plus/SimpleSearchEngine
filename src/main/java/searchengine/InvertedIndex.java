package searchengine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {
    private Map<String, Set<WebPage>> invertedIndex;
    
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

    public Set<WebPage> getPages(String word) {
        if (!invertedIndex.containsKey(word)) {
            return new HashSet<>();
        }
        return invertedIndex.get(word);
    }
}
