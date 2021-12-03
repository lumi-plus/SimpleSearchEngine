package searchengine;

import java.util.ArrayList;
import java.util.List;
//test condition false and true
//test 0 results, null
/**
 * returns a list "results" that contains hits, i.e. the number of search engine results for a specific query
 */
public class SearchEngine {
  public List<WebPage> searchPages(String searchTerm, FileReader fileReader) {
    var result = new ArrayList<WebPage>();
    for (var page : fileReader.getPages()) {
      if (page.getContent().contains(searchTerm)) {
        result.add(page);
      }
    }
    return result;
  }
}
