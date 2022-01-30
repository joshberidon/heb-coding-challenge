public class Image {
    int id;
    String label;
    String url;
    public Image(int id, String label, String url){
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
                '}';
    }
}
