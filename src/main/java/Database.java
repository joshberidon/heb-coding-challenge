import java.sql.*;
import java.util.List;

public class Database {
    private Connection conn;

    public Database(String s) {
        createNewDatabase(s);
        dropTables();
        createImageTable();
        createTagsTable();
        createImageTagJunctionTable();
    }

    private void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;

        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
            }

        } catch (SQLException e) {
            //todo
            throw new RuntimeException(e.getMessage());
        }
    }

    private ResultSet execute(String sql) {
        //make sure we  have a connection
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
            return statement.getResultSet();
        } catch (SQLException e) {
            //todo
            throw new RuntimeException(e.getMessage());
        }
    }

    private void dropTables() {
        execute("DROP TABLE IF EXISTS images");
        execute("DROP TABLE IF EXISTS tags");
        execute("DROP TABLE IF EXISTS image_tag_junction");
    }

    private void createImageTable() {
        String sql = "CREATE TABLE IF NOT EXISTS images (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	label text NOT NULL,\n"
                + "	image blob,\n"
                + "	url text\n"
                + ");";

        execute(sql);
    }

    private void createTagsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tags (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	tag text NOT NULL,\n"
                + " UNIQUE(tag)"
                + ");";

        execute(sql);
    }

    private void createImageTagJunctionTable() {
        String sql = "CREATE TABLE IF NOT EXISTS image_tag_junction (\n"
                + "	image integer NOT NULL,\n"
                + "	tag integer NOT NULL,\n"
                + " FOREIGN KEY(image) REFERENCES images(id), \n"
                + " FOREIGN KEY(tag) REFERENCES tags(id) \n"
                + ");";
        execute(sql);
    }

    private void createTag(String tag) {
        String sql = String.format("INSERT OR IGNORE INTO tags (tag) " +
                "values ('%s');", tag.toLowerCase());
        System.out.println(sql);
        execute(sql);
    }


    public void addImageByUrl(String url, String label, List<String> tags) {
        //todo add tag if does not exist and then create join
        tags.forEach(this::createTag);
        String sql = String.format(
                "INSERT INTO IMAGES " +
                        "(label, url)" +
                        "values " +
                        "('%s', '%s');",
                label, url);
        execute(sql);
    }

}