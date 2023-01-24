package test;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends test.TaskManagerTest<FileBackedTasksManager> {

    public FileBackedTasksManagerTest() {
        super();
    }

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager("src\\docs\\file.csv");
    }

    @Test
    public void shouldStandardBehaviorSaveFile() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        Task task2 = new Task("Task #2", "Description: Task #2", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);
        int taskId2 = manager.addNewTask(task2);

        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        Epic epic2 = new Epic("Epic #2", "Description: Epic #2");
        int epicId1 = manager.addNewEpic(epic1);
        int epicId2 = manager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, 3);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.NEW, 3);
        Subtask subtask3 = new Subtask("Subtask #3-e1", "Description: Subtask #3-e1", TaskStatus.NEW, 3);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        int subtaskId2 = manager.addNewSubtask(subtask2);
        int subtaskId3 = manager.addNewSubtask(subtask3);

        manager.getTask(taskId1);
        manager.getEpic(epicId1);
        manager.getSubtask(subtaskId1);
        manager.getHistory();

        Path expectedFile = FileBackedTasksManager.loadFromFile(manager.getFile()).getFile();

        assertEquals(manager.getFile(), expectedFile);
    }

    @Test
    public void shouldEmptySaveFileByEmptyListOfTask() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, 2);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        manager.getTask(taskId1);
        manager.getEpic(epicId1);
        manager.getSubtask(subtaskId1);
        manager.getHistory();

        manager.deleteTasks();
        manager.deleteEpics();

        Path expectedFile = FileBackedTasksManager.loadFromFile(manager.getFile()).getFile();

        assertEquals(manager.getFile(), expectedFile);
    }

    @Test
    public void shouldSaveFileByEmptyEpicSubtasks() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, 2);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        manager.getTask(taskId1);
        manager.getEpic(epicId1);
        manager.getSubtask(subtaskId1);
        manager.getHistory();

        manager.deleteSubtasks();

        Path expectedFile = FileBackedTasksManager.loadFromFile(manager.getFile()).getFile();

        assertEquals(manager.getFile(), expectedFile);
    }

    @Test
    public void shouldSaveFileByEmptyHistory() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, 2);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        Path expectedFile = FileBackedTasksManager.loadFromFile(manager.getFile()).getFile();

        assertEquals(manager.getFile(), expectedFile);
    }
}
