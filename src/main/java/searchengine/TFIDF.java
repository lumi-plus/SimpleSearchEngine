package searchengine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TFIDF extends TF {
    private InvertedIndex invertedIndex;

    public TFIDF(InvertedIndex invertedIndex) {
        super(invertedIndex);
        this.invertedIndex = invertedIndex;
    }

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
        return Math.log10((double) documents.size() / result.size()+1);
    }

    @Override
    public double computeRank(String term, WebPage document, Set<WebPage> documents) {
        double tf = super.termFrequency(term, document.getContent());
        double idf = inverseDocumentFrequency(term, documents);
        return tf * idf;
    }

}
