package searchengine;

import java.util.ArrayList;
import java.util.List;

public class SearchEngine {
    
    

    public List<List<String>> searchPages(String searchTerm, FileReader fileReader) {
        var result = new ArrayList<List<String>>();
        for (var page : fileReader.getPages()) {
          if (page.contains(searchTerm)) {
            result.add(page);
          }
        }
        return result;
      }
    


}
