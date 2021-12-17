package searchengine;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class TFTest {
    private InvertedIndex invertedIndex;
    private RankingAlgorithm tf;
    private FileReader fileReader;
    private QueryHandler queryHandler;

    @BeforeAll
    public void setup() {
        try {
            fileReader = new FileReader("data/enwiki-tiny.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        invertedIndex = new InvertedIndex(fileReader.getPages());
        tf = new TF(invertedIndex);
        queryHandler = new QueryHandler(invertedIndex);
    }

    @Test
    void findZeroTerms() {
        String term = "nohitter";
        Set<WebPage> pages = invertedIndex.getPages("usa");
        WebPage document = (WebPage) pages.toArray()[0];
        double actualRank = tf.computeRank(term, document, pages);
        assertEquals(0, actualRank);
    }


    @Test
    void findOneTerm() {
        String term = "usa";
        Set<WebPage> pages = invertedIndex.getPages("usa");
        WebPage document = (WebPage) pages.toArray()[0];
        List<String> content = document.getContent();
        int size = content.size();
        double expectedFrequency = 1.0 / size;
        double actualRank = tf.computeRank(term, document, pages);
        assertEquals(expectedFrequency, actualRank);
    }

    @Test
    void findThreeTerms() {
        String term = "federal";
        Set<WebPage> pages = invertedIndex.getPages(term);
        WebPage document = (WebPage) pages.toArray()[0];
        List<String> content = document.getContent();
        int size = content.size();
        double expectedFrequency = 3.0 / size;
        double actualFrequency = tf.computeRank(term, document, pages);
        assertEquals(expectedFrequency, actualFrequency);
    }

    @Test
    void findHighestRankedPage() {
        String fullQuery = "america%20OR%20archipelago";
        Set<WebPage> pages = queryHandler.getSearchResults(fullQuery);
        Map<WebPage, Double> ranked = tf.rank(pages, fullQuery);
        String actualTitle = tf.sortRanking(ranked).get(0).getTitle();
        String expectedTitle = "United States";
        assertEquals(expectedTitle, actualTitle);
    }

    @Test
    void findLowestRankedPage() {
        String fullQuery = "land";
        Set<WebPage> pages = queryHandler.getSearchResults(fullQuery);
        Map<WebPage, Double> ranked = tf.rank(pages, fullQuery);
        String actualTitle = tf.sortRanking(ranked).get(pages.size()-1).getTitle();
        String expectedTitle = "United States";
        assertEquals(expectedTitle, actualTitle);
    }

    @Test
    void rankNonExistingWebPage() {
        String query = "no-hit-query%20OR%usa";
        Set<WebPage> documents = queryHandler.getSearchResults(query);
        Map<WebPage, Double> nonExistingWebPage = tf.rank(documents, "no-hit-query");
        assertTrue(nonExistingWebPage.isEmpty());
    }

    @Test
    void testSortRankings() {
        Map<WebPage, Double> rankings = new HashMap<>();
        rankings.put(new WebPage("url", "Denmark", new ArrayList<>()), 2.0);
        rankings.put(new WebPage("url", "USA", new ArrayList<>()), 1.0);
        rankings.put(new WebPage("url", "Japan", new ArrayList<>()), 3.0);
        List<WebPage> sortedPages = tf.sortRanking(rankings);
        assertEquals("Japan", sortedPages.get(0).getTitle());
        assertEquals("Denmark", sortedPages.get(1).getTitle());
        assertEquals("USA", sortedPages.get(2).getTitle());
    }

}
