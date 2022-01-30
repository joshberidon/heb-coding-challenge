package Models;

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
