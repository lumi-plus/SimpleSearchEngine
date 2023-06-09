package searchengine;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class FileReaderTest {
    private FileReader fileReader;
    private List<WebPage> pages;

    @BeforeAll
    public void setup() throws IOException {
        fileReader = new FileReader("data/enwiki-tiny.txt");
        pages = fileReader.getPages();
    }

    @Test
    void setupFaulyFileReader() {
        FileReader faultyFileReader = null;
        try {
            faultyFileReader = new FileReader("data/not-a-file.txt");
        } catch (IOException e) {
            assertNull(faultyFileReader);
        }
    }

    @Test
    void testGetFile() {
        byte[] emptyFile = fileReader.getFile("not-a-filename");
        byte[] nonEmptyFile = fileReader.getFile("web/index.html");
        assertEquals(0, emptyFile.length);
        assertTrue(nonEmptyFile.length>0);
    }

    @Test
    void testSizeOfGetPages() {
        int size = fileReader.getPages().size();
        assertEquals(6, size);
    }

    @Test
    void testOrderOfGetPages() {
        assertEquals("United States", pages.get(0).getTitle());
        assertEquals("Denmark", pages.get(1).getTitle());
        assertEquals("Japan", pages.get(2).getTitle());
        assertEquals("Copenhagen", pages.get(3).getTitle());
        assertEquals("University of Copenhagen", pages.get(4).getTitle());
        assertEquals("IT University of Copenhagen", pages.get(5).getTitle());
    }

    @Test
    void testSizeOfContentOfWebPage() {
        WebPage USA = pages.get(0);
        int size = USA.getContent().size();
        assertEquals(157, size);
    }

    @Test
    void testFirstWordOfContentOfWebPage() {
        WebPage USA = pages.get(0);
        List<String> content = USA.getContent();
        assertEquals("the", content.get(0));
    }

    @Test
    void testLastWordOfContentOfWebPage() {
        WebPage USA = pages.get(0);
        List<String> content = USA.getContent();
        assertEquals("wildlife", content.get(content.size() - 1));
    }
}
