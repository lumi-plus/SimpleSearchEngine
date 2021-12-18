package searchengine;

import java.util.List;

/**
 * class to split documents only consisiting of strings into a title, url and
 * content to represent a web page
 * 
 * @author skje, lmig, mers, davv
 * @version 2021.12.15
 */
public class WebPage {
    // the title of the web page
    private String title;
    // the uniform resource locator of the web page
    private String url;
    // the content of the web page represented as a list of strings
    private List<String> content;

    /**
     * constructor of the web page
     * 
     * @param url     uniform resource locator of the web page
     * @param title   title of the web page
     * @param content content of the web page represented as a list of strings
     */
    public WebPage(String url, String title, List<String> content) {
        this.url = url;
        this.title = title;
        this.content = content;
    }

    /**
     * @return title of the web page
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @return content of the web page
     */
    public List<String> getContent() {
        return this.content;
    }

    /**
     * @return uniform resource locator of the web page
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * assigns content to the web page
     * 
     * @param content content of the web page represented as a list of strings
     */
    public void setContent(List<String> content) {
        this.content = content;
    }

    /**
     * assigns a title to the web page
     * 
     * @param title title of the web page
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * assigns a uniform resource locator to the web page
     * 
     * @param url uniform resource locator of the web page
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
