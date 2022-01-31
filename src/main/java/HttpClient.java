
import Models.Result;
import Models.ResultEnvelope;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public class HttpClient {
    private final OkHttpClient client;
    private final JsonClient jsonClient = new JsonClient();


    public HttpClient() {
        client = new OkHttpClient.Builder()
                .authenticator((route, response) -> {
                    if (response.request()
                            .header("Authorization") != null) {
                        return null;
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

            ResultEnvelope envelope = jsonClient.resultEnvelope(Objects.requireNonNull(response.body())
                    .source());

            if (envelope != null) {
                return envelope.result;
            }

        }
        return null;
    }
}
