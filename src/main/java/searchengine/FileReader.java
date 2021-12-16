package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * class responsible for reading the files in the database
 * 
 * @author skje, lmig, mers, davv
 * @version 2021.12.15
 */
public class FileReader {
    // contains all web pages consisting of url, title, content
    private List<WebPage> pages = new ArrayList<>();

    /**
     * creates a file reader that goes through all the files in the database and
     * adds web pages
     * based on the files to ArrayList pages
     * 
     * @param filename name of the file from the database which is being read
     * @throws IOException
     */
    public FileReader(String filename) throws IOException {
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
    }

    /**
     * 
     * @param filename name of the file from the database which is being read
     * @return Array of bytes
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
     * get the list containing all web pages
     * 
     * @return ArrayList with all files in webpage format with url, title and
     *         content
     */
    public List<WebPage> getPages() {
        return pages;
    }

}
