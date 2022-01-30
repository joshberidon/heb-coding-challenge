
import Models.Result;
import Models.ResultEnvelope;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class HttpClient {
    private final OkHttpClient client;
    private final Moshi moshi = new Moshi.Builder().build();
    private final JsonAdapter<Gist> gistJsonAdapter = moshi.adapter(Gist.class);
    private final JsonAdapter<ResultEnvelope> tagsJsonAdapter = moshi.adapter(ResultEnvelope.class);

    public HttpClient() {
        client = new OkHttpClient.Builder()
                .authenticator((route, response) -> {
                    if (response.request()
                            .header("Authorization") != null) {
                        return null; // Give up, we've already attempted to authenticate.
                    }

                    //todo move to secrets, these are just free creds
                    String credential = Credentials.basic("acc_651748049528795", "1326e11aeef5bf082efc6d5df1a5566b");
                    return response.request()
                            .newBuilder()
                            .header("Authorization", credential)
                            .build();
                })
                .build();
    }


    public Result getImageObjectsUrl(String url) throws Exception {
        Request request = new Request.Builder()
                .url("https://api.imagga.com/v2/tags" + "?image_url=" + url)
                .build();

        try (Response response = client.newCall(request)
                .execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            ResultEnvelope envelope = tagsJsonAdapter.fromJson(Objects.requireNonNull(response.body())
                    .source());

            if (envelope != null) {
                return envelope.result;
            }

        }
        return null;
    }


    public void run() throws Exception {
        Request request = new Request.Builder()
                .url("https://api.github.com/gists/c2a7c39532239ff261be")
                .build();
        try (Response response = client.newCall(request)
                .execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            Gist gist = gistJsonAdapter.fromJson(response.body()
                    .source());

            for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue().content);
            }
        }
    }

    static class Gist {
        Map<String, GistFile> files;
    }

    static class GistFile {
        String content;
    }
}
