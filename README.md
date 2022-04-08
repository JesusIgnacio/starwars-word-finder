# Finding Luke through the command line by consuming an API

In the last chapter of this series, I need to find a Starwars word related, in the response of an API, a rarely need, yes.

I have to find the word **Luke** in a known Starwars API, called [Swapi](https://swapi.dev/), without IDEs, or frameworks but I’ll use the last chapter's pieces of knowledge

Having the next class:

```java
public class SearchingInStarWarsFor {
    
    public static void main(String[] args) {
        if (args.length > 0)
            StarWarsWordFinder.searchingFor(args[0]);
    }
}
```

This StarWarsWordFinder has next specifications:

- Execute a request(GET) to the Swapi(people scope).
- It searches if the API body response would contain my searched word.

To achieve both objectives, I only need the JDK library that contains:

- [java.net](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/net/package-summary.html) package that contains:
    - [URI](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/net/URI.html) → to adressing the request.
    - [http](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/package-summary.html) sub-package with:
        - [HttpClient](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html) → to send request and retrieve it response.
        - [HttpRequest](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpRequest.html) → to build a request.
        - [HttpResponse](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpResponse.html) → response object.

Getting a similar code like this:

```java
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
        System.out.println(String.format("%s was found %s times", word,  foundTimes));
    }

    static void searchingIn(String results, String arg) {
        if (results.contains(arg))
            foundTimes++;
    }
}
```

I compile this source to generate a Library as explained in Creating a Java Library chapter, and I run the following terminal command:

```bash
java -cp .\starWarsWordFinder.jar .\SearchingInStarWarsFor.java Luke
#it prints
Luke was found 1 times
```

Let’s try with Yoda:

```bash
java -cp .\starWarsWordFinder.jar .\SearchingInStarWarsFor.java Yoda         
#it prints
Yoda was found 0 times
```

Oh! “*Forget someone seems the API...”🤣*

Let’s try with Vader:

```bash
java -cp .\starWarsWordFinder.jar .\SearchingInStarWarsFor.java Vader         
#it prints
Vader was found 1 times
```

*“I’m your API Daddy...*” Ok, enough of older StarWars jokes!

Let's try something else, backing to the code of the library. I could add an external library, to convert API string json response to an object, making it a little more specific in terms of only people's name scope.

The [Gson](https://github.com/google/gson) library could parse the string response to a JSON.

Let’s see the source of StarWarsWordFinder code after adding Gson’s fromJson() method:

```java
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class StarWarsWordFinderWithGson {

    //same as last version class

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
```

It was necessary to create two additional classes:

- StarWarsPeople
- StarWarsPerson

To map the string results to the valid structure.

So, running the following commands:

```bash
javac -cp .\gson-2.9.0.jar  -d build .\StarWarsWordFinderWithGson.java #to get .class files
jar --create --file starWarsWordFinder.jar .\StarWarsWordFinderWithGson.class '.\StarWarsWordFinderWithGson$StarWarsPeople.class' 
'.\StarWarsWordFinderWithGson$StarWarsPerson.class' #to create the jar file
```

I get the new version of my library StarWarsWordFinderWithGson then I can run the main class, adding 2 jars, the gson and the starWarsWordFinder libraries:

```bash
java -cp "starWarsWordFinder.jar;gson-2.9.0.jar" .\SearchingInStarWarsWithGson.java Luke
#it prints
Luke was found 1 times
```

So, resuming this in a nutshell

- A word needs to be found.
- Identify tools inside JDK library to achivet it.
- Build a java library with it.
- Instance and call it inside our main program.
- Compile it and running it
- Make some starwars boring joke...ok. no.

This is the end of handmade series , I covered:

- Run a Java program with related jar.
- Create a jar.
- Build a service using a known framework.
- Consume an API, create a jar and use it to build a word finder java program.

I hope you enjoyed it like I enjoyed writing it. There are a lot more coming soon.

**Tech stack**

- Java 11.
- Windows 10.

**Repo**

[https://github.com/JesusIgnacio/starwars-word-finder](https://github.com/JesusIgnacio/starwars-word-finder)
