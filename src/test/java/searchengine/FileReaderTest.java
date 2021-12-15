package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class FileReaderTest {
    private FileReader fileReader;
    private List<WebPage> pages;

    @BeforeAll
    public void setUp() {
        try {
            fileReader = new FileReader("data/enwiki-tiny.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        pages = fileReader.getPages();
    }

    @Test
    public void testSizeOfGetPages() {
        int size = fileReader.getPages().size();
        assertEquals(6, size);
    }

    @Test
    public void testOrderOfGetPages() {
        assertEquals("United States", pages.get(0).getTitle());
        assertEquals("Denmark", pages.get(1).getTitle());
        assertEquals("Japan", pages.get(2).getTitle());
        assertEquals("Copenhagen", pages.get(3).getTitle());
        assertEquals("University of Copenhagen", pages.get(4).getTitle());
        assertEquals("IT University of Copenhagen", pages.get(5).getTitle());
    }

    @Test
    public void testSizeOfContentOfWebPage() {
        WebPage USA = pages.get(0);
        int size = USA.getContent().size();
        assertEquals(157, size);
    }

    @Test
    public void testFirstWordOfContentOfWebPage() {
        WebPage USA = pages.get(0);
        List<String> content = USA.getContent();
        assertEquals("the", content.get(0));
    }

    @Test
    public void testLastWordOfContentOfWebPage() {
        WebPage USA = pages.get(0);
        List<String> content = USA.getContent();
        assertEquals("wildlife", content.get(content.size() - 1));
    }
}
