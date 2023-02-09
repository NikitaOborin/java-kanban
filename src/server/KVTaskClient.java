package server;

import com.google.gson.*;
import model.Task;
import model.TaskStatus;
import service.InMemoryTasksManager;
import service.TasksManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KVTaskClient {
    private final String urlKVServer;
    private final String apiToken;

    public KVTaskClient(String urlKVServer) {
        this.urlKVServer = urlKVServer;   //например -  http://localhost:8078/load/Tasks?API_TOKEN=1675612271011
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
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
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
            System.out.println("Значение по ключу " + key + " не найдено!");
            return "";
        }
    }

    public static void main(String[] args) {
        KVServer kvServer = new KVServer();
        kvServer.start();
//        kvServer.stop();

        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078");

        TasksManager manager = new InMemoryTasksManager();

        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        Task task2 = new Task("Task #2", "Description: Task #2", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 15));
        int taskId1 = manager.addNewTask(task1);
        int taskId2 = manager.addNewTask(task2);

        List<Task> tasks = manager.getListOfTasks();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls().setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        kvTaskClient.put("tasks", gson.toJson(tasks));

        String outputStringTaskList = kvTaskClient.load("tasks");
        JsonElement elementOfOutputStringTaskList = JsonParser.parseString(outputStringTaskList);
        JsonArray arrayOfElementOfOutputStringTaskList = elementOfOutputStringTaskList.getAsJsonArray();
        ArrayList<Task> expectedListOfTask = new ArrayList<>();
        for (JsonElement jsonElement : arrayOfElementOfOutputStringTaskList) {
            Task task = gson.fromJson(jsonElement, Task.class);
            expectedListOfTask.add(task);
        }

        System.out.println(expectedListOfTask);

        Task updTask1 = new Task("updTask #1", "Description: updTask #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 3, 0));
        updTask1.setId(1);
        Task updTask2 = new Task("updTask #2", "Description: updTask #2", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 4, 15));
        updTask2.setId(2);
        manager.updateTask(updTask1);
        manager.updateTask(updTask2);

        kvTaskClient.put("tasks", gson.toJson(manager.getListOfTasks()));

        String outputStringTaskList2 = kvTaskClient.load("tasks");
        JsonElement elementOfOutputStringTaskList2 = JsonParser.parseString(outputStringTaskList2);
        JsonArray arrayOfElementOfOutputStringTaskList2 = elementOfOutputStringTaskList2.getAsJsonArray();
        ArrayList<Task> expectedListOfTask2 = new ArrayList<>();
        for (JsonElement jsonElement : arrayOfElementOfOutputStringTaskList2) {
            Task task = gson.fromJson(jsonElement, Task.class);
            expectedListOfTask2.add(task);
        }

        System.out.println(expectedListOfTask2);
    }
}
