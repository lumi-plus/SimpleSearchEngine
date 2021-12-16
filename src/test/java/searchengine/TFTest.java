package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
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
    void termFrequencyTest() {
        TF tf = new TF(this.invertedIndex);
        String term = "usa";
        Set<WebPage> pages = this.invertedIndex.getPages("usa");
        var a = pages.toArray();
        WebPage document = (WebPage) a[0];
        List<String> content = document.getContent();
        int size = content.size();
        double expectedFrequency = 1.0 / size;
        double actualFrequency = tf.termFrequency(term, content);
        assertEquals(expectedFrequency, actualFrequency);
    }

    @Test
    void termFrequencyTest2() {
        TF tf = new TF(this.invertedIndex);
        String term = "federal";
        Set<WebPage> pages = this.invertedIndex.getPages(term);
        var a = pages.toArray();
        WebPage document = (WebPage) a[0];
        List<String> content = document.getContent();
        int size = content.size();
        double expectedFrequency = 3.0 / size;
        double actualFrequency = tf.termFrequency(term, content);
        assertEquals(expectedFrequency, actualFrequency);

    }

    @Test
    void rankTest() {
        TF tf = new TF(this.invertedIndex);
        String fullQuery = "denmark";
        Set<WebPage> pages = this.invertedIndex.getPages(fullQuery);
        Map<WebPage, Double> ranked = tf.rank(pages, fullQuery);
        String actualTitle = tf.sortRanking(ranked).get(0).getTitle();
        String expectedTitle = "Denmark";
        assertEquals(expectedTitle, actualTitle);

    }

    @Test
    void rankTest2() {
        TF tf = new TF(this.invertedIndex);
        String fullQuery = "denmarkn";
        Set<WebPage> pages = this.invertedIndex.getPages(fullQuery);
        Map<WebPage, Double> ranked = tf.rank(pages, fullQuery);
        String actualTitle = tf.sortRanking(ranked).get(0).getTitle();
        String expectedTitle = "Denmark";
        assertEquals(expectedTitle, actualTitle);

    }

    @Test
    void computeRankTest() {
        TF tf = new TF(this.invertedIndex);
        String term = "usa";
        Set<WebPage> pages = this.invertedIndex.getPages("usa");
        var a = pages.toArray();
        WebPage document = (WebPage) a[0];
        List<String> content = document.getContent();
        int size = content.size();
        double expectedFrequency = 1.0 / size;
        double actualFrequency = tf.computeRank(term, document, pages);
        assertEquals(expectedFrequency, actualFrequency);

    }

    @Test
    void rankTest3() {
        TF tf = new TF(this.invertedIndex);
        String falseQuery = "false";
        String trueQuery = "usa";
        Set<WebPage> pages = this.invertedIndex.getPages(trueQuery);
        var a = pages.toArray();
        WebPage document = (WebPage) a[0];
        var x = tf.computeRank(falseQuery, document, pages);
        assertEquals(0, x);
    }

    @Test
    void rankTest4() {
        TF tf = new TF(this.invertedIndex);
        String falseQuery = "false";
        String trueQuery = "usa";
        Set<WebPage> pages = this.invertedIndex.getPages(trueQuery);
        var x = tf.rank(pages, falseQuery).get(trueQuery);
        assertEquals(null, x);
    }

    @Test
    void rankTest5() {
        TF tf = new TF(this.invertedIndex);
        QueryHandler queryHandler = new QueryHandler(this.invertedIndex);
        String fullQuery = "usa%20OR%20usa";
        Set<WebPage> pages = queryHandler.getSearchResults(fullQuery);
        Map<WebPage, Double> ranked = tf.rank(pages, fullQuery);
        String actualTitle = tf.sortRanking(ranked).get(0).getTitle();
        String expectedTitle = "United States";
        assertEquals(expectedTitle, actualTitle);
    }

    @Test
    void rankTest6() {
        TF tf = new TF(this.invertedIndex);
        QueryHandler queryHandler = new QueryHandler(this.invertedIndex);
        String fullQuery = "empty%20OR%20false";
        Set<WebPage> pages = queryHandler.getSearchResults(fullQuery);
        Map<WebPage, Double> ranked = tf.rank(pages, fullQuery);
        int actualSize = tf.sortRanking(ranked).size();
        int expectedSize = 0;
        assertEquals(expectedSize, actualSize);
    }

}
