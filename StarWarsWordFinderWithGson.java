import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class StarWarsWordFinderWithGson {

    static final String STARWARS_URL_PEOPLE_API = "https://swapi.dev/api/people";
    static int foundTimes = 0;

    public static void search(String arg) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder(URI.create(STARWARS_URL_PEOPLE_API))
                .GET()
                .build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(results -> searchingIn(results, arg))
                .join();
        System.out.println(String.format("%s were found %s times", arg,  foundTimes));
    }

    static void searchingIn(String results, String arg) {
        Gson gson = new Gson();
        StarWarsPeople starWarsPeople = gson.fromJson(results, StarWarsPeople.class);
        for(StarWarsPerson starWarsPerson: starWarsPeople.getResults()) {
            if (starWarsPerson.getName().contains(arg))
                foundTimes++;
        }
    }

    class StarWarsPeople {
        private List<StarWarsPerson> results;
        public List<StarWarsPerson> getResults() {
            return results;
        }
    }

    class StarWarsPerson {
        private String name;
        public String getName() {
            return name;
        }
    }
}
