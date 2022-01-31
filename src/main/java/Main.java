import Models.Image;
import Models.PostImage;
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
        JsonClient jsonClient = new JsonClient();

        app.get("/images", ctx -> {
            try {
                //todo sanitize
                String query = ctx.queryParam("objects");
                if (query != null) {
                    List<String> objects = Arrays.asList(
                            query.split(","));
                    ctx.result(database.getImagesByTags(objects)
                            .toString());
                } else {
                    ctx.status(200);
                    ctx.result(jsonClient.getListImageJson(database.getImages()));
                }
            } catch (Exception e) {
                ctx.status(500);
            }
        });

        app.get("/images/{id}", ctx -> {
            //todo sanitize
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                Image image = database.getImageById(id);
                if (image != null) {
                    ctx.status(200);
                    ctx.result(jsonClient.getImageJson(image));
                } else {
                    ctx.status(404);
                }
            } catch (Exception e) {
                ctx.status(500);
            }

        });

        app.post("/images", ctx -> {
            try {
                //todo sanitize
                PostImage postImageReq = jsonClient.postImageFromJson(ctx.body());

                String url = postImageReq.url;
                String label = postImageReq.label;
                boolean detect = postImageReq.detection;
                List<String> tags = new ArrayList<>();
                if (detect) {
                    tags = httpClient.getImageObjectsUrl(url).tags.stream()
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
                ctx.status(200);
                ctx.result(jsonClient.getImageJson(database.addImageByUrl(url, label, tags)));
            } catch (Exception e) {
                ctx.status(500);
            }
        });
    }
}
