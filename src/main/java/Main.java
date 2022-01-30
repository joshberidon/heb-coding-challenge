import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);
        app.get("/", ctx -> ctx.result("Hello World"));

        app.get("/images", ctx -> ctx.result("Hello World"));
        app.get("/images/query", ctx -> ctx.result("Hello World"));
        app.get("/images/id", ctx -> ctx.result("Hello World"));
        app.post("/images", ctx -> ctx.result(""));


        Database database = new Database("heb.sqlite");

        database.addImageByUrl("url", "label", Arrays.asList("cat", "dog"));
    }

}
