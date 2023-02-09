import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;
import server.HttpTaskManager;
import server.HttpTaskServer;
import server.KVServer;
import service.Managers;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
//        KVServer kvServer = new KVServer();
//        kvServer.start();
//        kvServer.stop();
//
//        HttpTaskServer httpTaskServer = new HttpTaskServer();
//        httpTaskServer.startHttpTaskServer();
//        httpTaskServer.stopHttpTaskServer();
//
//        HttpTaskManager httpTaskManager = httpTaskServer.getHttpTaskManager();
//
//        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(15),
//                LocalDateTime.of(2022, 12, 12, 1, 0));
//        Task task2 = new Task("Task #2", "Description: Task #2", TaskStatus.NEW, Duration.ofMinutes(15),
//                LocalDateTime.of(2022, 12, 12, 1, 15));
//        int taskId1 = httpTaskManager.addNewTask(task1);
//        int taskId2 = httpTaskManager.addNewTask(task2);
//
//        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
//        Epic epic2 = new Epic("Epic #2", "Description: Epic #2");
//        int epicId1 = httpTaskManager.addNewEpic(epic1);
//        int epicId2 = httpTaskManager.addNewEpic(epic2);
//
//        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW,
//                Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 12, 1, 30), epicId1);
//        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.NEW,
//                Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 12, 1, 45), epicId1);
//        Subtask subtask3 = new Subtask("Subtask #3-e1", "Description: Subtask #3-e1", TaskStatus.NEW,
//                Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 12, 2, 0), epicId1);
//        int subtaskId1 = httpTaskManager.addNewSubtask(subtask1);
//        int subtaskId2 = httpTaskManager.addNewSubtask(subtask2);
//        int subtaskId3 = httpTaskManager.addNewSubtask(subtask3);
//
//        System.out.println(httpTaskManager.getTasks());
//        System.out.println(httpTaskManager.getEpics());
//        System.out.println(httpTaskManager.getSubtasks());
//
//        System.out.println(httpTaskManager.getTask(taskId1));
//        System.out.println(httpTaskManager.getEpic(epicId1));
//        System.out.println(httpTaskManager.getSubtask(subtaskId1));
//
//        System.out.println(httpTaskManager.getHistory());
    }
}
