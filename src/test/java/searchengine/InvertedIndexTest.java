package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class InvertedIndexTest {
    private List<WebPage> pages;
    private InvertedIndex invertedIndex;
    private FileReader fileReader;
    
    @BeforeAll
    public void setup() throws IOException {
        fileReader = new FileReader("data/enwiki-tiny.txt");
        pages = fileReader.getPages();
        invertedIndex = new InvertedIndex(pages);
    }

    @Test
    public void lookUpWordWithZeroResults() {
        assertEquals(0, invertedIndex.getPages("pingu").size());
    }

    @Test
    public void lookUpWordWithOneResult() {
        assertEquals(1, invertedIndex.getPages("selfgoverning").size());
    }
    
    @Test
    public void lookUpWordWithSixResults() {
        assertEquals(6, invertedIndex.getPages("the").size());
    }
}
