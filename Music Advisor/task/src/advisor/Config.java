package advisor;

public class Config {
    public static int PAGINATION = 5;
    public static String SERVER_PATH = "https://accounts.spotify.com";
    public static String API_SERVER_PATH = "https://api.spotify.com";

    public static String REDIRECT_URI = "http://localhost:8080";
    public static String CLIENT_ID = "your_client_ID";
    public static String CLIENT_SECRET = "your_client_secret";

    public static String ACCESS_TOKEN = "";
    public static String AUTH_CODE = "";

    public static final String CATEGORIES = "/v1/browse/categories";
    public static final String NEW_RELEASES = "/v1/browse/new-releases";
    public static final String FEATURED = "/v1/browse/featured-playlists";
    public static final String CATEGORY = "/v1/browse/categories/{category_id}/playlists";

    public static final String UNAUTHORIZED = "Please, provide access for application.";
}
