package searchengine;

import java.util.List;
import java.util.Set;

public abstract class RankAlgoritm {

    public abstract List<WebPage> rank(Set<WebPage> pages, String fullQuery);
}
