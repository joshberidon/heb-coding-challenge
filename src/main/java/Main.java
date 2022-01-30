import Models.Image;
import Models.Tag;
import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create()
                .start(7070);
        Database database = new Database("heb.sqlite");
        HttpClient httpClient = new HttpClient();

        app.get("/images", ctx -> {
            //todo sanitize
            ctx.result(database.getImages()
                    .toString());
        });

        app.get("/images?objects={query}", ctx -> {
            //todo sanitize
            List<String> objects = Arrays.asList(
                    ctx.pathParam("query")
                            .split(","));
            ctx.result(database.getImagesByObjects(objects)
                    .toString());
        });

        app.get("/images/{id}", ctx -> {
            //todo sanitize
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.result(database.getImageById(id)
                    .toString());
        });

        app.post("/images", ctx -> {
            //todo get url, label from ctx
            String url = "";
            String label = "";
            boolean detect = true;
            List<String> tags = new ArrayList<>();
            if (detect) {
                tags = httpClient.getImageObjectsUrl("https://i.imgur.com/YgRLlok.jpeg").tags.stream()
                        .map(Tag::getTag)
                        .collect(Collectors.toList());
            }
            if (label == null || label.isEmpty()) {
                if (tags.size() > 0) {
                    label = tags.get(0);
                } else {
                    label = "unknown";
                }
            }
            Image image;
            if (url != null) {
                image = database.addImageByUrl(url, label, tags);
            } else {
                image = database.addImageByUrl(url, label, tags);
            }

            ctx.result(image.toString());
        });

        database.addImageByUrl("cat", "label", Arrays.asList("cat", "dog"));
        database.addImageByUrl("pig", "label", Arrays.asList("pig", "horse"));
        database.addImageByUrl("donkey", "label", Arrays.asList("donkey", "pig"));
    }

}
