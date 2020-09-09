package advisor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIRequests {

    public static HttpResponse<String> send_GET_request(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Config.ACCESS_TOKEN)
                .uri(URI.create(Config.API_SERVER_PATH + endpoint))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

}
