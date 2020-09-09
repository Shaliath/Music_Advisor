package advisor.processing;

import advisor.View;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ProcessFeatured implements Processing {
    @Override
    public void process(HttpResponse<String> response) {
        JsonObject jsonObject = JsonParser.parseString(response.body())
                .getAsJsonObject()
                .getAsJsonObject("playlists");
        ArrayList<String> featured = new ArrayList<>();

        for (JsonElement album : jsonObject.getAsJsonArray("items")) {
            StringBuilder builder = new StringBuilder();
            builder.append(album.getAsJsonObject().get("name").getAsString())
                    .append("\n")
                    .append(album.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString())
                    .append("\n\n");
            featured.add(builder.toString());
        }
        View.getInstance().setData(featured.toArray(String[]::new));
    }
}
