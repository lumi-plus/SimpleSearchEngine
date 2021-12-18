package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class TFIDFTest {
    private InvertedIndex invertedIndex;
    private List<WebPage> pages;
    private TFIDF rankingAlgorithm;
    private QueryHandler queryHandler;

    @BeforeAll
    public void setup() throws IOException {
        FileReader fileReader = new FileReader("data/enwiki-tiny.txt");
        pages = fileReader.getPages();
        invertedIndex = new InvertedIndex(pages);
        rankingAlgorithm = new TFIDF(invertedIndex);
        queryHandler = new QueryHandler(invertedIndex);
    }
    
    @Test
    void rankForZeroHits() {
        String noHitter = "not a query that exist in any document";
        String query = noHitter+"%20OR%20the";
        Set<WebPage> documents = queryHandler.getSearchResults(query);
        double rank = rankingAlgorithm.inverseDocumentFrequency(noHitter, documents);
        double expected = Math.log10(6.0);
        assertEquals(expected, rank);
    }

    @Test
    void oneOutOfSixIDF() {
        String query = "usa%20OR%20the";
        Set<WebPage> documents = queryHandler.getSearchResults(query);
        double rank = rankingAlgorithm.inverseDocumentFrequency("usa", documents);
        double expected = Math.log10(6.0/2);
        assertEquals(expected, rank);
    }

    @Test
    void twoOutOfSixIDF() {
        String query = "university%20OR%20the";
        Set<WebPage> documents = queryHandler.getSearchResults(query);
        double rank = rankingAlgorithm.inverseDocumentFrequency("university", documents);
        double expected = Math.log10(6.0/3);
        assertEquals(expected, rank);
    }

    @Test
    void sixOutOfSixIDF() {
        String query = "the";
        Set<WebPage> documents = queryHandler.getSearchResults(query);
        double rank = rankingAlgorithm.inverseDocumentFrequency(query, documents);
        double expected = Math.log10(6.0/7);
        assertEquals(expected, rank);
    }

    @Test
    void computeRankWorks() {
        String query = "the";
        Set<WebPage> documents = queryHandler.getSearchResults(query);
        WebPage document = (WebPage) documents.toArray()[0];
        double tf = rankingAlgorithm.termFrequency(query, document.getContent());
        double idf = rankingAlgorithm.inverseDocumentFrequency(query, documents);
        double actual = rankingAlgorithm.computeRank(query, document, documents);
        assertEquals(tf*idf, actual);
    }
 
}
