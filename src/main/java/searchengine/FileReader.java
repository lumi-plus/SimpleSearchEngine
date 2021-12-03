package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
  private List<List<String>> pages = new ArrayList<>();

  public FileReader(String filename) throws IOException {
    try {
      List<String> lines = Files.readAllLines(Paths.get(filename));
      var firstIndex = 0;
      for (var i = 1; i < lines.size(); i++) {
        if (lines.get(i).startsWith("*PAGE")) {
          pages.add(lines.subList(firstIndex, i));
          firstIndex = i;
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public byte[] getFile(String filename) {
    try {
      return Files.readAllBytes(Paths.get(filename));
    } catch (IOException e) {
      e.printStackTrace();
      return new byte[0];
    }
  }

  public List<List<String>> getPages() {
    return pages;
  }

}
