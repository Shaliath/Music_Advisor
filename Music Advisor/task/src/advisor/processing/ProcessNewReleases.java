package advisor.processing;

import advisor.View;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessNewReleases implements Processing {
    @Override
    public void process(HttpResponse<String> response) {

        JsonObject jsonObject = JsonParser.parseString(response.body())
                .getAsJsonObject()
                .getAsJsonObject("albums");
        ArrayList<String> newReleases = new ArrayList<>();
        for (JsonElement album : jsonObject.getAsJsonArray("items")) {
            StringBuilder builder = new StringBuilder();
            builder.append(album.getAsJsonObject().get("name").getAsString())
                    .append("\n");

            ArrayList<String> artists = new ArrayList<>();
            for (JsonElement artist : album.getAsJsonObject().getAsJsonArray("artists")) {
                artists.add(artist.getAsJsonObject().get("name").getAsString());
            }
            builder.append(Arrays.toString(artists.toArray()))
                    .append("\n")
                    .append(album.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString())
                    .append("\n\n");
            newReleases.add(builder.toString());
        }
        View.getInstance().setData(newReleases.toArray(String[]::new));
    }
}
