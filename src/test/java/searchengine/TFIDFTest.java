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
    public void idf() {
        String query = "usa%20OR%20the";
        Set<WebPage> documents = queryHandler.getSearchResults(query);
        Set<WebPage> usa = invertedIndex.getPages("usa");
        double rank = rankingAlgorithm.inverseDocumentFrequency("usa", documents);
        assertEquals(Math.log10(6/2), rank);
    }

}
