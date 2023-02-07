package test;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskManager;
import server.HttpTaskServer;
import server.KVServer;
import service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends test.TaskManagerTest<HttpTaskManager> {

    KVServer kvServer = new KVServer();
    HttpTaskServer httpTaskServer = new HttpTaskServer();
    HttpTaskManager httpTaskManager;
    HttpTaskManager httpTaskManager2;

    public HttpTaskManagerTest() {
        super();
    }

//    @BeforeEach
//    public void startServers() {
//        kvServer.start();
//        httpTaskServer.startHttpTaskServer();
//    }

    @AfterEach
    public void stopKVServer() {
        kvServer.stop();
    }

    @Test
    public void shouldGetTasksFromServer() {           // не запускается тест, не могу понять в чем ошибка
        kvServer.start();
        httpTaskServer.startHttpTaskServer();

        httpTaskManager = Managers.getDefaultHttpTaskManager();

        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        Task task2 = new Task("Task #2", "Description: Task #2", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 15));
        int taskId1 =  httpTaskManager.addNewTask(task1);
        int taskId2 = httpTaskManager.addNewTask(task2);

        int size1 = httpTaskManager.getTasks().size();

        httpTaskManager2 = Managers.getDefaultHttpTaskManager();
        int size2 = httpTaskManager2.getTasks().size();

        assertEquals(size1, size2);
    }
}
