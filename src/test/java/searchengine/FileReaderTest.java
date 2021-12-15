package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class FileReaderTest {
    private FileReader fileReader;

    @BeforeAll
    public void setUp() {
        try {
            // var filename = "data/test-file.txt";
            // String filename = Files.readString(Paths.get("config.txt")).strip();
            fileReader = new FileReader("data/enwiki-tiny.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @Test
    // public void testGetFile() {
    // assertno
    // assertNotNull(fileReader.getFile("enwiki-tiny.txt"));
    // // assertNull(fileReader.getFile("inappropriate/filepath"));
    // }

    @Test
    public void testGetPages() {
        int size = fileReader.getPages().size();
        var a = fileReader.getPages();
        assertEquals("United States", a.get(0).getTitle());
        assertEquals("Denmark", a.get(1).getTitle());
        assertEquals("Japan", a.get(2).getTitle());
        assertEquals("Copenhagen", a.get(3).getTitle());
        assertEquals("University of Copenhagen", a.get(4).getTitle());
        assertEquals("IT University of Copenhagen", a.get(5).getTitle());
        assertNotEquals("anything other than Japan", a.get(2).getTitle());
        assertEquals(6, size);
    }

}
