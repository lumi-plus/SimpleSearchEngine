package searchengine;

import java.io.IOException;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

// @TestInstance(Lifecycle.PER_CLASS)
public class FileReaderTest {
    private FileReader fileReader = null;

    @BeforeAll
    public void setUp() {
        try {
            fileReader = new FileReader("data/test-file.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetFile() {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("data/test-file.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
