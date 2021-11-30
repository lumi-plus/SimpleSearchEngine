package searchengine;

import java.util.List;

public class WebPage {
    private String title;
    private String url;
    private List<String> content;
    
    public String getTitle() {
        return title;
    }

    public List<String> getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
