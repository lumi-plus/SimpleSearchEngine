package searchengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFIDF {
    private Map<WebPage, Integer> rankedPages;

    public TFIDF(List<WebPage> pages, String term) {
        rankedPages = new HashMap<>();
    }

    public double termFrequency(String term, List<String> document) {
        double count = 0;
        for (String word : document) {
            if (term.equals(word)) {
                count++;
            }
        }
        return count/document.size();
    }

    public double inverseDocumentFrequency(String term, List<WebPage> documents) {
        double count = 0;
        for (WebPage document: documents) {
            for (String word : document.getContent()) {
                if (word.equals(term)) {
                    count++;
                    break;
                }
            }
        }
        return count/documents.size();
    }

    public double getTFID(String term, WebPage document, List<WebPage> documents) {
        double tf = termFrequency(term, document.getContent());
        double idf = inverseDocumentFrequency(term, documents);
        return tf*idf;
    }

    public Map<WebPage, Integer> getRankedResults() {
        return rankedPages;
    }
}
