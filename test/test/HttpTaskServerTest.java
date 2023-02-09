package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;
import server.HttpTaskManager;
import server.HttpTaskServer;
import server.KVServer;
import service.Managers;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;


import static org.junit.jupiter.api.Assertions.*;


public class HttpTaskServerTest {

    HttpClient client = HttpClient.newHttpClient();
    Gson gson = Managers.getGson();
    static KVServer kvServer = new KVServer();
    HttpTaskServer httpTaskServer = new HttpTaskServer();

    @BeforeAll
    public static void startServers() {
        kvServer.start();
    }

    @AfterEach
    public void stopKVServer() {
        kvServer.stop();
        httpTaskServer.stopHttpTaskServer();
    }

    @Test
    public void shouldStatusCode200ByAddTask() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/task");
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void shouldStatusCode200ByAddEpic() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void shouldStatusCode200ByAddSubtask() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask");
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 12, 3, 0), 1);
        String jsonSubtask = gson.toJson(subtask1);
        HttpRequest.BodyPublisher bodySubtask = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest requestSubtask = HttpRequest.newBuilder().
                uri(urlSubtask).
                POST(bodySubtask).
                build();
        HttpResponse<String> responseSubtask = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseSubtask.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void shouldStatusCode400ByAddTaskWithIncorrectLink() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/qwety");
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), HttpURLConnection.HTTP_BAD_REQUEST);
    }

    @Test
    public void shouldStatusCode200ByGetTasks() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/task");
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGet = URI.create("http://localhost:8080/tasks/task");
        HttpRequest requestGet = HttpRequest.newBuilder().uri(urlGet).GET().build();
        HttpResponse<String> responseGet  = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseGet.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void shouldStatusCode200ByGetEpics() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGet = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest requestGet = HttpRequest.newBuilder().uri(urlGet).GET().build();
        HttpResponse<String> responseGet  = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseGet.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void shouldStatusCode200ByGetSubtasks() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask");
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 12, 3, 0), 1);
        String jsonSubtask = gson.toJson(subtask1);
        HttpRequest.BodyPublisher bodySubtask = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest requestSubtask = HttpRequest.newBuilder().
                uri(urlSubtask).
                POST(bodySubtask).
                build();
        HttpResponse<String> responseSubtask = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseSubtask.statusCode(), HttpURLConnection.HTTP_OK);

        URI urlGet = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest requestGet = HttpRequest.newBuilder().uri(urlGet).GET().build();
        HttpResponse<String> responseGet  = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseGet.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void shouldStatusCode200ByGetTaskWithId() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/task");
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGet = URI.create("http://localhost:8080/tasks/task?id=" + 1);
        HttpRequest requestGet = HttpRequest.newBuilder().uri(urlGet).GET().build();
        HttpResponse<String> responseGet  = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseGet.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void shouldStatusCode200ByGetEpicWithId() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGet = URI.create("http://localhost:8080/tasks/epic?id=" + 1);
        HttpRequest requestGet = HttpRequest.newBuilder().uri(urlGet).GET().build();
        HttpResponse<String> responseGet  = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseGet.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void shouldStatusCode200ByGetSubtaskWithId() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask");
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 12, 3, 0), 1);
        String jsonSubtask = gson.toJson(subtask1);
        HttpRequest.BodyPublisher bodySubtask = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest requestSubtask = HttpRequest.newBuilder().
                uri(urlSubtask).
                POST(bodySubtask).
                build();
        HttpResponse<String> responseSubtask = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseSubtask.statusCode(), HttpURLConnection.HTTP_OK);

        URI urlGet = URI.create("http://localhost:8080/tasks/subtask?id=" + 2);
        HttpRequest requestGet = HttpRequest.newBuilder().uri(urlGet).GET().build();
        HttpResponse<String> responseGet  = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseGet.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void shouldStatusCode200ByGetEpicSubtasksId() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask");
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 12, 3, 0), 1);
        String jsonSubtask = gson.toJson(subtask1);
        HttpRequest.BodyPublisher bodySubtask = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest requestSubtask = HttpRequest.newBuilder().
                uri(urlSubtask).
                POST(bodySubtask).
                build();
        HttpResponse<String> responseSubtask = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseSubtask.statusCode(), HttpURLConnection.HTTP_OK);

        URI urlGet = URI.create("http://localhost:8080/tasks/subtask/epic?id=" + 1);
        HttpRequest requestGet = HttpRequest.newBuilder().uri(urlGet).GET().build();
        HttpResponse<String> responseGet  = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseGet.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public  void shouldStatusCode200ByGetHistory() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/task");
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGet = URI.create("http://localhost:8080/tasks/task?id=" + 1);
        HttpRequest requestGet = HttpRequest.newBuilder().uri(urlGet).GET().build();
        HttpResponse<String> responseGet  = client.send(requestGet, HttpResponse.BodyHandlers.ofString());

        URI urlGetHistory = URI.create("http://localhost:8080/tasks/history");
        HttpRequest requestGetHistory = HttpRequest.newBuilder().uri(urlGetHistory).GET().build();
        HttpResponse<String> responseGetHistory  = client.send(requestGetHistory, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseGetHistory.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public  void shouldStatusCode200ByGetPrioritizedTasks() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url1 = URI.create("http://localhost:8080/tasks/task");
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        String json1 = gson.toJson(task1);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().
                uri(url1).
                POST(body1).
                build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/task");
        Task task2 = new Task("Task #2", "Description: Task #2", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 2, 0));
        String json2 = gson.toJson(task2);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().
                uri(url2).
                POST(body2).
                build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request= HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response  = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void shouldStatusCode200ByDeleteTaskWithId() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/task");
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlDelete = URI.create("http://localhost:8080/tasks/task?id=" + 1);
        HttpRequest requestDelete = HttpRequest.newBuilder().uri(urlDelete).GET().build();
        HttpResponse<String> responseDelete  = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseDelete.statusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void shouldStatusCode400ByDeleteTaskWithIncorrectId() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/task");
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlDelete = URI.create("http://localhost:8080/tasks/task?id=" + 2);
        HttpRequest requestDelete = HttpRequest.newBuilder().uri(urlDelete).GET().build();
        HttpResponse<String> responseDelete  = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseDelete.statusCode(), HttpURLConnection.HTTP_BAD_REQUEST);
    }

    @Test
    public void shouldStatusCode200ByDeleteAllTasks() throws IOException, InterruptedException {
        httpTaskServer.startHttpTaskServer();

        URI url = URI.create("http://localhost:8080/tasks/task");
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(body).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlDelete = URI.create("http://localhost:8080/tasks/task");
        HttpRequest requestDelete = HttpRequest.newBuilder().uri(urlDelete).GET().build();
        HttpResponse<String> responseDelete  = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseDelete.statusCode(), HttpURLConnection.HTTP_OK);
    }
}