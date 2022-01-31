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

    private int getTagId(String tag) {
        //todo combine into one query with create Tag
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }
        String sql = String.format("select id  \n" +
                "from tags where tag = '%s';", tag);
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            return rs.getInt(1);
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
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
            int id = statement.getGeneratedKeys()
                    .getInt(1);
            image = new Image(id, label, url);
        } catch (SQLException e) {
            //todo
            throw new RuntimeException(e.getMessage());
        }

        tags.forEach(s -> {
            updateImageTagJunctionTable(image.id, getTagId(s));
        });
        return image;
    }

    public void updateImageTagJunctionTable(int imageId, int tagId) {
        String sql = String.format("INSERT OR IGNORE INTO image_tag_junction (image, tag) " +
                "values ('%s', '%s');", imageId, tagId);
        execute(sql);
    }

    public Image getImageById(int id) {
        Image image;
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }
        String sql = String.format("SELECT images.id, images.image, images.label, images.url \n" +
                        "FROM images WHERE id = '%s';"
                , id);
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            String label = rs.getString("label");
            String url = rs.getString("url");
            image = new Image(id, label, url);
        } catch (SQLException e) {
            //todo
            throw new RuntimeException(e.getMessage());
        }

        //todo make this more efficient via sql
        image.tags = getTagsByImageId(image.id);
        return image;
    }

    public List<Image> getImages() {
        ArrayList<Image> images = new ArrayList<>();
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }
        String sql = "SELECT images.id, images.image, images.label, images.url FROM images";
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

        //todo make this more efficient via sql
        images.forEach(image -> {
            image.tags = getTagsByImageId(image.id);
        });
        return images;
    }

    private List<Integer> getImageIdsByTagId(int tagId) {
        List<Integer> imageIds = new ArrayList<>();
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }
        String sql = String.format("SELECT DISTINCT image, tag \n" +
                "from image_tag_junction WHERE tag = '%s';", tagId);
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int imageId = rs.getInt("image");
                imageIds.add(imageId);
            }
        } catch (SQLException e) {
            //todo
            throw new RuntimeException(e.getMessage());
        }
        return imageIds;
    }

    public List<Image> getImagesByTags(List<String> tags) {
        List<Image> images = new ArrayList<>();
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }

        tags.forEach(s -> {
            int tagId = getTagId(s);
            List<Integer> imageIds = getImageIdsByTagId(tagId);
            System.out.println(imageIds);
            imageIds.forEach(integer -> {
                if (images.stream()
                        .noneMatch(o -> o.id == (integer))) {
                    Image image = getImageById(integer);
                    image.tags = tags;
                    images.add(image);
                }
            });
        });

        return images;
    }

    public List<String> getTagsByImageId(int imageId) {
        List<String> tags = new ArrayList<>();
        if (conn == null) {
            throw new RuntimeException("No database connection");
        }
        String sql = String.format("SELECT image_tag_junction.tag, tags.tag from image_tag_junction \n" +
                "INNER JOIN tags ON image_tag_junction.tag = tags.id WHERE image_tag_junction.image = '%s';", imageId);
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String tag = rs.getString(2);
                tags.add(tag);
            }
        } catch (SQLException e) {
            //todo
            throw new RuntimeException(e.getMessage());
        }
        return tags;
    }
}