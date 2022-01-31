package Models;

public class PostImage {
    public String url;
    public String label;
    public boolean detection;

    @Override
    public String toString() {
        return "PostImage{" +
                "url='" + url + '\'' +
                ", label='" + label + '\'' +
                ", detection=" + detection +
                '}';
    }
}
