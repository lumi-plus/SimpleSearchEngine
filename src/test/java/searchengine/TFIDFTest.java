package searchengine;

import java.io.IOException;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class TFIDFTest {
    private InvertedIndex invertedIndex;

    @BeforeAll
    public void setup() throws IOException {
        FileReader fileReader = new FileReader("data/enwiki-tiny.txt");
        invertedIndex = new InvertedIndex(fileReader.getPages());
    }

    @Test
    
}
