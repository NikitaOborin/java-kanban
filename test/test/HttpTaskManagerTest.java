package test;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskManager;
import server.KVServer;
import service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends test.TaskManagerTest<HttpTaskManager> {

    KVServer kvServer = new KVServer();
    HttpTaskManager expectedHttpTaskManager;

    public HttpTaskManagerTest() {
        super();
    }

    @BeforeEach
    public void createManagerAndStartKVServer() {
        kvServer.start();
        manager = Managers.getDefaultHttpTaskManager();
    }

    @AfterEach
    public void stopKVServer() {
        kvServer.stop();
    }

    @Test
    public void shouldStandardBehaviorSaveAndLoadManagerOnKVServer() {
        initTasks();

        manager = Managers.getDefaultHttpTaskManager();
        manager.getTask(taskId1);
        manager.getEpic(epicId1);
        manager.getSubtask(subtaskId1);
        manager.getHistory();

        expectedHttpTaskManager = Managers.getDefaultHttpTaskManager();

        assertEquals(manager.getTask(taskId1), expectedHttpTaskManager.getTask(taskId1));
    }

    @Test
    public void shouldEmptySaveAndLoadManagerOnKVServerByEmptyListOfTask() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        int taskId1 = manager.addNewTask(task1);
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 12, 3, 0), epicId1);;
        int subtaskId1 = manager.addNewSubtask(subtask1);

        manager.getTask(taskId1);
        manager.getEpic(epicId1);
        manager.getSubtask(subtaskId1);
        manager.getHistory();
        manager.deleteTasks();
        manager.deleteEpics();

        expectedHttpTaskManager = Managers.getDefaultHttpTaskManager();

        assertEquals(manager.getTasks(), expectedHttpTaskManager.getTasks());
        assertEquals(manager.getSubtasks(), expectedHttpTaskManager.getSubtasks());
        assertEquals(manager.getEpics(), expectedHttpTaskManager.getEpics());
    }

    @Test
    public void shouldSaveAndLoadManagerOnKVServerByEmptyEpicSubtasks() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        int taskId1 = manager.addNewTask(task1);
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 12, 3, 0), epicId1);;
        int subtaskId1 = manager.addNewSubtask(subtask1);

        manager.getTask(taskId1);
        manager.getEpic(epicId1);
        manager.getSubtask(subtaskId1);
        manager.getHistory();
        manager.deleteSubtasks();

        expectedHttpTaskManager = Managers.getDefaultHttpTaskManager();

        assertEquals(manager.getEpic(epicId1), expectedHttpTaskManager.getEpic(epicId1));
    }

    @Test
    public void shouldSaveAndLoadFileByEmptyHistory() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2022, 12, 12, 1, 0));
        int taskId1 = manager.addNewTask(task1);
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 12, 3, 0), epicId1);;
        int subtaskId1 = manager.addNewSubtask(subtask1);

        expectedHttpTaskManager = Managers.getDefaultHttpTaskManager();

        assertEquals(manager.getTasks(), expectedHttpTaskManager.getTasks());
        assertEquals(manager.getSubtasks(), expectedHttpTaskManager.getSubtasks());
        assertEquals(manager.getEpics(), expectedHttpTaskManager.getEpics());
    }
}
