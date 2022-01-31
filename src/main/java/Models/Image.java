package Models;

import java.util.ArrayList;
import java.util.List;

public class Image {
    public int id;
    public String label;
    public String url;
    public List<String> tags;

    public Image(int id, String label, String url) {
        this.id = id;
        this.label = label;
        this.url = url;

    }

    public Image(int id, String label, String url, List<String> tags) {
        this.id = id;
        this.label = label;
        this.url = url;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", url='" + url + '\'' +
                ", tags=" + tags +
                '}';
    }
}
