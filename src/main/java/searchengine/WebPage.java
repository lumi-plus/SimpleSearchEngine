package searchengine;

import java.util.List;

public class WebPage {
    private String title;
    private String url;
    private List<String> content;

    public WebPage(String url, String title, List<String> content) {
        this.url = url;
        this.title = title;
        this.content = content;
    }
    
    /**
     * returns the titel of the webpage
     * @return
     */
    public String getTitle() {
        return title;
    }
    /**
     * returns the content of the webpage
     * @return
     */
    public List<String> getContent() {
        return content;
    }
    /**
     * returns the URL of the webpage
     * @return
     */
    public String getUrl() {
        return url;
    }
    /**
     * sets the content
     * @param content
     */
    public void setContent(List<String> content) {
        this.content = content;
    }
    /**
     * sets the title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * sets the URL
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
