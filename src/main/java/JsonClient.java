import Models.Image;
import Models.PostImage;
import Models.ResultEnvelope;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonClient {
    private final Moshi moshi = new Moshi.Builder().build();
    private final JsonAdapter<ResultEnvelope> tagsJsonAdapter = moshi.adapter(ResultEnvelope.class);
    private final JsonAdapter<Image> imageJsonAdapter = moshi.adapter(Image.class);
    private final JsonAdapter<PostImage> postImageJsonAdapter = moshi.adapter(PostImage.class);
    Type type = Types.newParameterizedType(List.class, Image.class);
    private final JsonAdapter<List<Image>> jsonAdapter = moshi.adapter(type);


    public ResultEnvelope resultEnvelope(BufferedSource source) throws IOException {
        return tagsJsonAdapter.fromJson(source);
    }

    public String getListImageJson(List<Image> images) {
        return jsonAdapter.toJson(images);
    }

    public String getImageJson(Image image) {
        return imageJsonAdapter.toJson(image);
    }

    public PostImage postImageFromJson(String string) throws IOException {
        return postImageJsonAdapter.fromJson(string);
    }
}
