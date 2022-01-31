package Models;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Image {
    public int id;
    public String label;
    public String url;
    public List<String> tags;

    public Image(int id, String label, String url, List<String> tags) {
        this.id = id;
        this.label = label;
        this.url = url;
        this.tags = tags;
    }

    public Image(int id, String label, String url) {
        this.id = id;
        this.label = label;
        this.url = url;

    }

    public void addTag(String tag){
       ArrayList<String> temp = new ArrayList<>();
       temp.add(tag);
       temp.addAll(tags);
       tags = temp;
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
