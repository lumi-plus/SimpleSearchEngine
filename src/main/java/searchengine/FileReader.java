package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    private List<WebPage> pages = new ArrayList<>();

    public FileReader(String filename) throws IOException {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            int firstIndex = 0;
            for (int i = 1; i < lines.size(); i++) {
                if (lines.get(i).startsWith("*PAGE")) {
                    String url = lines.get(firstIndex).substring("*PAGE:".length());
                    String title = lines.get(firstIndex + 1);
                    List<String> content = lines.subList(firstIndex + 2, i);
                    if (!(title.isBlank() && content.isEmpty())) {
                        pages.add(new WebPage(url, title, content));
                        firstIndex = i;
                    }
                }
            }
            String url = lines.get(firstIndex).substring("*PAGE:".length());
            String title = lines.get(firstIndex + 1);
            List<String> content = lines.subList(firstIndex + 2, lines.size());
            if (!(title.isBlank() && content.isEmpty())) {
                pages.add(new WebPage(url, title, content));
            }
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
     * 
     * @return
     */
    public List<WebPage> getPages() {
        System.out.println("Total count all pages in file: " + pages.size());
        return pages;
    }

}
