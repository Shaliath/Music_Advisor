package advisor.processing;

import advisor.View;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ProcessPlaylists implements Processing {
    @Override
    public void process(HttpResponse<String> response) {
        JsonObject jo = JsonParser.parseString(response.body())
                .getAsJsonObject()
                .getAsJsonObject("playlists");
        ArrayList<String> playlists = new ArrayList<>();
        for (JsonElement playlist : jo.getAsJsonArray("items")) {
            StringBuilder builder = new StringBuilder();
            builder.append(playlist.getAsJsonObject().get("name").getAsString()).append("\n");
            builder.append(playlist.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString()).append("\n\n");
            playlists.add(builder.toString());
        }
        View.getInstance().setData(playlists.toArray(String[]::new));
    }
}
