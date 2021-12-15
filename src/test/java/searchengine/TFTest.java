package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class TFTest {
    private InvertedIndex invertedIndex;

    private FileReader fileReader;

    @BeforeAll
    public void setup() {
        try {
            fileReader = new FileReader("data/enwiki-tiny.txt");
            this.invertedIndex = new InvertedIndex(fileReader.getPages());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void computeFrequencyTest() {
        TF tf = new TF(this.invertedIndex);
        String term = "USA";
        Set<WebPage> pages = this.invertedIndex.getPages("usa");
        var a = pages.toArray();
        WebPage document = (WebPage) a[0];
        int size = document.getContent().size();
        List<String> content = document.getContent();
        var x = content.get(0);
        var y = content.get(156);
        var expectedFrequency = tf.termFrequency(term, content);
        assertEquals((1 / size), expectedFrequency);
    }

    @Test
    public void computeFrequencyTest2() {
        TF tf = new TF(this.invertedIndex);
        String term = "federal";
        Set<WebPage> pages = this.invertedIndex.getPages("usa");
        var a = pages.toArray();
        WebPage document = (WebPage) a[0];
        int size = document.getContent().size();
        List<String> content = document.getContent();
        var x = content.get(0);
        var y = content.get(156);
        var expectedFrequency = tf.termFrequency(term, content);
        assertEquals((1 / size), expectedFrequency);

    }

}
