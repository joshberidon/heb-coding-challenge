import io.javalin.Javalin;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        Javalin app = Javalin.create()
                .start(7070);
        Database database = new Database("heb.sqlite");

        app.get("/", ctx -> ctx.result("Hello World"));
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
            ctx.result("Hello World");

        });


        database.addImageByUrl("cat", "label", Arrays.asList("cat", "dog"));
        database.addImageByUrl("pig", "label", Arrays.asList("pig", "horse"));
        database.addImageByUrl("donkey", "label", Arrays.asList("donkey", "pig"));
    }

}
