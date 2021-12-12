package searchengine;

public class TFIDF {
    public double termFrequency(String term, List<String> document) {
        double count = 0;
        for (String word : document) {
            if (term.equals(word)) {
                count++;
            }
        }
        return count/document.size();
    }

    public double inverseDocumentFrequency(String term, List<WebPage> documents) {
        double count = 0;
        for (WebPage document: documents) {
            for (String word : document.getContent()) {
                if (word.equals(term)) {
                    count++;
                    break;
                }
            }
        }
        return count/documents.size();
    }

    public double getTFID(String term, WebPage document, List<WebPage> documents) {
        double tf = termFrequency(term, document.getContent());
        double idf = inverseDocumentFrequency(term, documents);
        return tf*idf;
    }
}
