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
public class TFIDFTest {
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
    public void oneOutOfSixIDF() {
        String query = "usa%20OR%20the";
        Set<WebPage> documents = queryHandler.getSearchResults(query);
        double rank = rankingAlgorithm.inverseDocumentFrequency("usa", documents);
        double expected = Math.log10(6.0/2);
        assertEquals(expected, rank);
    }

    @Test
    public void twoOutOfSixIDF() {
        String query = "university%20OR%20the";
        Set<WebPage> documents = queryHandler.getSearchResults(query);
        double rank = rankingAlgorithm.inverseDocumentFrequency("university", documents);
        double expected = Math.log10(6.0/3);
        assertEquals(expected, rank);
    }

    @Test
    public void sixOutOfSixIDF() {
        String query = "the";
        Set<WebPage> documents = queryHandler.getSearchResults(query);
        double rank = rankingAlgorithm.inverseDocumentFrequency("the", documents);
        double expected = Math.log10(6.0/7);
        assertEquals(expected, rank);
    }

}
