package searchengine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * class to calculate the inverse document frequency and term frequency-inverse document frequency to sort search results
 * based on this score
 * @author skje, lmig, mers, davv
 * @version 2021.12.15
 */
public class TFIDF extends TF {
    //the inverted index maps content to a given word
    private InvertedIndex invertedIndex;

    /**
     * constructor for TFIDF
     * @param invertedIndex maps content to a given word
     */
    public TFIDF(InvertedIndex invertedIndex) {
        super(invertedIndex);
        this.invertedIndex = invertedIndex;
    }

    /**
     * calculates the inverse document frequency by taking the logarithm of the amount of all documents 
     * divided by the amount of documents that contain the search term (only applicable for queries containing "OR")
     * @param term query for which to calculate the frequency
     * @param documents set of web pages in which the search term of the query occurs
     * @return the inverse document frequency
     */
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
        // double score = 
        return Math.log10((double) documents.size() / (result.size()+1));
    }

    /** 
     * calculates the term frequncy-inverse document frequency score for a given query
     * @param term query for which to calculate the frequency
     * @param document list of strings representing the file in which the search term occurs
     * @param documents set of web pages in which the search term of the query occurs
     * @return the tfidf score
     */
    @Override
    public double computeRank(String term, WebPage document, Set<WebPage> documents) {
        double tf = super.termFrequency(term, document.getContent());
        double idf = inverseDocumentFrequency(term, documents);
        return tf * idf;
    }
}
