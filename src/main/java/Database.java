import Models.Image;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection conn;

    public Database(String s) {
        createNewDatabase(s);
        dropTables();
        createImagesTable();
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

    private void execute(String sql) {
        //make sure we  have a connection
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
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

    private void createImagesTable() {
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


    private int createTag2(String tag) {
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }
        String sql = String.format("INSERT OR IGNORE INTO tags (tag) VALUES ('%s') \n" +
                "SELECT id FROM tags;", tag.toLowerCase());
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            int id = rs.getInt("id");
            System.out.println("Tag " + tag + " id: " + id);
            return id;
        } catch (SQLException e) {
            //todo
            throw new RuntimeException(e.getMessage());
        }
    }


    private void createTag(String tag) {
        String sql = String.format("INSERT OR IGNORE INTO tags (tag) " +
                "values ('%s');", tag.toLowerCase());
        execute(sql);
    }

    public Image addImageByUrl(String url, String label, List<String> tags) {
        //todo add tag if does not exist and then create join
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }
        Image image;
        tags.forEach(this::createTag);

        String sql = String.format(
                "INSERT INTO images " +
                        "(label, url) " +
                        "VALUES " +
                        "('%s', '%s');",
                label, url);
        System.out.println(sql);
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
            int id = statement.getGeneratedKeys().getInt(1);
            System.out.println(id);
            image = new Image(id, label, url);
        } catch (SQLException e) {
            //todo
            throw new RuntimeException(e.getMessage());
        }

        tags.forEach(s -> {
            updateImageTagJunctionTable(image.id, 1);
        });
        return image;
    }

    public void updateImageTagJunctionTable(int imageId, int tagId){
        String sql = String.format("INSERT OR IGNORE INTO image_tag_junction (image, tag) " +
                "values ('%s', '%s');", imageId, tagId);
        execute(sql);
    }

    public Image getImageById(int id) {
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }
        String sql = String.format("select id, image, label, url \n" +
                "from images where id = '%s';", id);
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            String label = rs.getString("label");
            String url = rs.getString("url");
            return new Image(id, label, url);
        } catch (SQLException e) {
            //todo
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Image> getImages() {
        ArrayList<Image> images = new ArrayList<>();
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }
        String sql = "select id, image, label, url \n" +
                "from images";
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String label = rs.getString("label");
                String url = rs.getString("url");
                images.add(new Image(id, label, url));
            }
        } catch (SQLException e) {
            //todo
            throw new RuntimeException(e.getMessage());
        }
        return images;
    }

    public List<Image> getImagesByObjects(List<String> objects) {
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }
        //get image ids from join table
        //return all image id rows
        return null;
    }
}