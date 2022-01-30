package Models;

import java.util.List;


public class Result {
    public List<Tag> tags;

    public Result(List<Tag> results) {
        this.tags = results;
    }

    @Override
    public String toString() {
        return "Result{" +
                "tags=" + tags +
                '}';
    }
}


