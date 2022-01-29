import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);
        app.get("/", ctx -> ctx.result("Hello World"));

        app.get("/images", ctx -> ctx.result("Hello World"));
        app.get("/images/query", ctx -> ctx.result("Hello World"));
        app.get("/images/id", ctx -> ctx.result("Hello World"));

    }
}
