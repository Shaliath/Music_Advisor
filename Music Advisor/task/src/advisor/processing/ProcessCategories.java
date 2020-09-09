package advisor.processing;

import advisor.View;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ProcessCategories implements Processing {
    @Override
    public void process(HttpResponse<String> response) {
        JsonObject jsonObject = JsonParser.parseString(response.body())
                .getAsJsonObject()
                .getAsJsonObject("categories");
        ArrayList<String> categories = new ArrayList<>();
        for (JsonElement category : jsonObject.getAsJsonArray("items")) {
            categories.add(category.getAsJsonObject().get("name").getAsString() + "\n");
        }
        View.getInstance().setData(categories.toArray(String[]::new));
    }
}
