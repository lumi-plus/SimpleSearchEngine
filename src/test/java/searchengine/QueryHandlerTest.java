package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class QueryHandlerTest {
    private QueryHandler queryHandler;
    private InvertedIndex invertedIndex;
    private FileReader  fileReader;
    
    @BeforeAll
    public void setup(){
        try{
            fileReader = new FileReader("data/enwiki-tiny.txt");
            //fileReader = new FileReader("data/test-file.txt");
            invertedIndex = new InvertedIndex(fileReader.getPages());
            queryHandler = new QueryHandler(invertedIndex);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void singleWordSingleQuery(){
        int size = queryHandler.getSearchResults("usa").size();
        assertEquals(1, size);
    }

    @Test
    public void twoWordsSingleQuery(){
        int size = queryHandler.getSearchResults("country%20europe").size();
        assertEquals(1, size);
    }

    @Test
    public void singleWordDoubleQuery(){
        int size = queryHandler.getSearchResults("country%20OR%20listenidnmrk").size();
        assertEquals(3, size);
    }


    @Test
    public void doubleWordDoubleQuery(){
        int size = queryHandler.getSearchResults("country%20usa%20OR%20listenidnmrk%20dnm").size();
        assertEquals(2, size);
    }



    @Test
    public void tripleWordDoubleQuery(){
        int size = queryHandler.getSearchResults("japan%20is%20island%20OR%20usa%20OR%20happiest").size();
        assertEquals(4, size);
    }




    @Test
    public void doubleWordTripleQueryCapital(){
        int size = queryHandler.getSearchResults("Country%20usa%20OR%20listeNidnmrk%20dnm").size();
        assertEquals(2, size);
    }





}
