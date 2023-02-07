package server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String urlKVServer;
    private final String apiToken;

    public KVTaskClient(String urlKVServer) {
        this.urlKVServer = urlKVServer;   // http://localhost:8078  - например -  http://localhost:8078/load/Tasks/?API_TOKEN=1675612271011
        apiToken = createApiToken();
    }

    private String createApiToken() {
        try {
            URI url = URI.create(urlKVServer + "/register");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // сохранять состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN=
    public void put(String key, String json) {
        try {
            URI url = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // возвращать состояние менеджера задач через запрос GET /load/<ключ>?API_TOKEN=
    public String load(String key) {
        try {
            URI url = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
