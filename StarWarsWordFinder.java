import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
             
public class StarWarsWordFinder {

    static final String STARWARS_URL_PEOPLE_API = "https://swapi.dev/api/people";
    static int foundTimes = 0;
    
    public static void searchingFor(String word) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder(URI.create(STARWARS_URL_PEOPLE_API))
                .GET()
                .build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                      .thenApply(HttpResponse::body)
                        .thenAccept(results -> searchingIn(results, word))
                        .join();
        System.out.println(String.format("%s were found %s times", word,  foundTimes));
    }

    static void searchingIn(String results, String arg) {
        if (results.contains(arg))
            foundTimes++;
    }
}