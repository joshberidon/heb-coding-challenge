package Models;

import java.util.Map;

public class Tag {
    public double confidence;
    public Map<String, String> tag;

    public Tag(double confidence, Map<String, String> tag) {
        this.confidence = confidence;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "confidence=" + confidence +
                ", tag=" + tag +
                '}';
    }

    public String getTag() {
        return tag.get("en");
    }
}
