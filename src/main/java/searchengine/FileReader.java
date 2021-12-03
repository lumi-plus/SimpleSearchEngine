package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileReader {
  private List<List<String>> pages = new ArrayList<>();
  //consider making an extra method instead of a constructor for testability
  //try clause and each catch clause must be executed
  //for loop: 0, 1 and more iterations
  public FileReader(String filename) throws IOException {
    try {
      List<String> lines = Files.readAllLines(Paths.get(filename));
      var firstIndex = 0;
      for (var i = 1; i < lines.size(); i++) {
        if (lines.get(i).startsWith("*PAGE")) {
          pages.add(lines.subList(firstIndex, i+1));
          firstIndex = i;
        }
      }
      //Collections.reverse(pages);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
  /**
   * calls readAllBytes method and returns an array of bytes
   */
  public byte[] getFile(String filename) {
    try {
      return Files.readAllBytes(Paths.get(filename));
    } catch (IOException e) {
      e.printStackTrace();
      return new byte[0];
    }
  }
  /**
   * returns the list of Strings "pages"
   * @return
   */
  public List<List<String>> getPages() {
    return pages;
  }

}
