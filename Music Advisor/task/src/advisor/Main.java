package advisor;

import advisor.processing.ProcessCategories;
import advisor.processing.ProcessFeatured;
import advisor.processing.ProcessNewReleases;
import advisor.processing.ProcessPlaylists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    private static View view = View.getInstance();

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length > 1 && args[0].equals("-access")) {
            Config.SERVER_PATH = args[1];
        }
        if (args.length > 3 && args[2].equals("-resource")) {
            Config.API_SERVER_PATH = args[3];
        }
        if (args.length > 5 && args[4].equals("-page")) {
            Config.PAGINATION = Integer.parseInt(args[5]);
        }
        Scanner myScanner = new Scanner(System.in);
        boolean run = true;
        while (run) {
            String command = myScanner.nextLine().toLowerCase();
            if (command.startsWith("playlists")) {
                System.out.println(getPlaylists(command));
            } else {
                switch (command) {
                    case "auth":
                        auth();
                        break;
                    case "new":
                        System.out.println(getNewReleases());
                        break;
                    case "featured":
                        System.out.println(getFeatured());
                        break;
                    case "categories":
                        System.out.println(getCategories());
                        break;
                    case "exit":
                        run = false;
                        System.out.println("---GOODBYE!---");
                        break;
                    case "next":
                        System.out.println(view.getNextPage());
                        break;
                    case "prev":
                        System.out.println(view.getPreviousPage());
                        break;
                    default:
                        System.out.println("404 - Unknown command");
                        break;
                }
            }
        }
    }

    static void auth() {
        Authorization auth = new Authorization();
        auth.createHttpServer();
        auth.authRequest();
    }

    private static boolean notAuthorized() {
        return Config.ACCESS_TOKEN.isEmpty();
    }

    private static String getCategoryID(String categoryName) throws IOException, InterruptedException {
        HttpResponse<String> response = APIRequests.send_GET_request(Config.CATEGORIES);
        if (response.statusCode() != 200) {
            return getErrorMessage(response);
        }
        JsonObject jsonObject = JsonParser.parseString(response.body())
                .getAsJsonObject()
                .getAsJsonObject("categories");
        String categoryID = null;
        for (JsonElement category : jsonObject.getAsJsonArray("items")) {
            if (categoryName.equalsIgnoreCase(category.getAsJsonObject().get("name").getAsString())) {
                categoryID = category.getAsJsonObject().get("id").getAsString();
                break;
            }
        }
        return categoryID;
    }

    private static String getErrorMessage(HttpResponse<String> response) {
        JsonObject jsonObject = JsonParser.parseString(response.body())
                .getAsJsonObject()
                .getAsJsonObject("error");
        return jsonObject.get("message").getAsString();
    }

    private static int getStatusCode(HttpResponse<String> response) {
        if (response.body().contains("error")) {
            JsonObject jsonObject = JsonParser.parseString(response.body())
                    .getAsJsonObject()
                    .getAsJsonObject("error");
            return jsonObject.get("status").getAsInt();
        } else {
            return response.statusCode();
        }
    }

    private static String getPlaylists(String command) throws IOException, InterruptedException {
        if (notAuthorized()) {
            return Config.UNAUTHORIZED;
        }
        String categoryName = command.substring(10);
        String categoryID = getCategoryID(categoryName);
        if (categoryID == null) {
            return "Unknown category name.";
        }

        HttpResponse<String> response = APIRequests.send_GET_request(Config.CATEGORY.replace("{category_id}", categoryID));
        if (getStatusCode(response) != 200) {
            return getErrorMessage(response);
        }
        new ProcessPlaylists().process(response);
        return view.getFirstPage();
    }

    private static String getFeatured() throws IOException, InterruptedException {
        if (notAuthorized()) {
            return Config.UNAUTHORIZED;
        }
        HttpResponse<String> response = APIRequests.send_GET_request(Config.FEATURED);
        if (response.statusCode() != 200) {
            return getErrorMessage(response);
        }
        new ProcessFeatured().process(response);
        return view.getFirstPage();
    }

    private static String getCategories() throws IOException, InterruptedException {
        if (notAuthorized()) {
            return Config.UNAUTHORIZED;
        }
        HttpResponse<String> response = APIRequests.send_GET_request(Config.CATEGORIES);
        if (response.statusCode() != 200) {
            return getErrorMessage(response);
        }
        new ProcessCategories().process(response);
        return view.getFirstPage();
    }

    private static String getNewReleases() throws IOException, InterruptedException {
        if (notAuthorized()) {
            return Config.UNAUTHORIZED;
        }
        HttpResponse<String> response = APIRequests.send_GET_request(Config.NEW_RELEASES);
        if (response.statusCode() != 200) {
            return getErrorMessage(response);
        }
        new ProcessNewReleases().process(response);
        return view.getFirstPage();
    }
}
