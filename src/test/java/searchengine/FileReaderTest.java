package searchengine;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

// @TestInstance(Lifecycle.PER_CLASS)
public class FileReaderTest {
    private FileReader fileReader = null;

    @BeforeAll
    public void setUp() {
        try {
            var filename = Files.readString(Paths.get("config.txt")).strip();
            fileReader = new FileReader(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetFile() {
        assertNotNull(fileReader.getFile("data/test-file.txt"));
        // assertNull(fileReader.getFile("inappropriate/filepath"));
    }
}
