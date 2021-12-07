package searchengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndex {
    private Map<String, List<WebPage>> invertedIndex;

    public InvertedIndex(List<WebPage> pages) {
        invertedIndex = new HashMap<>();
        for(WebPage page : pages) {
            List<String> content = page.getContent();
            for (String word : content) {
                if (invertedIndex.containsKey(word)) {
                    // var p = invertedIndex.get(word);
                    // p.add(page);
                    // invertedIndex.put(word, p);
                    invertedIndex.get(word).add(page);
                } else {
                    List<WebPage> p = new ArrayList<>();
                    p.add(page);
                    invertedIndex.put(word, p);
                }
            }
        }
    }

    public List<WebPage> getPages(String word) {
        return invertedIndex.get(word);
    }

    // public Map<String, List<WebPage>> getInvertedIndex() {
    //     return invertedIndex;
    // }
}
