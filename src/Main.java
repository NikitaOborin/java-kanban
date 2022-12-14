import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task1 = new Task("Task #1", "Description: Task #1", Status.NEW);
        Task task2 = new Task("Task #2", "Description: Task #2", Status.NEW);
        int taskId1 = inMemoryTaskManager.addNewTask(task1);
        int taskId2 = inMemoryTaskManager.addNewTask(task2);

        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        Epic epic2 = new Epic("Epic #2", "Description: Epic #2");
        int epicId1 = inMemoryTaskManager.addNewEpic(epic1);
        int epicId2 = inMemoryTaskManager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", Status.NEW, 3);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", Status.NEW, 3);
        Subtask subtask3 = new Subtask("Subtask #3-e1", "Description: Subtask #3-e1", Status.NEW, 3);
        int subtaskId1 = inMemoryTaskManager.addNewSubtask(subtask1);
        int subtaskId2 = inMemoryTaskManager.addNewSubtask(subtask2);
        int subtaskId3 = inMemoryTaskManager.addNewSubtask(subtask3);

        inMemoryTaskManager.getTask(taskId1);
        inMemoryTaskManager.getTask(taskId2);
        inMemoryTaskManager.getEpic(epicId1);
        inMemoryTaskManager.getEpic(epicId2);
        inMemoryTaskManager.getSubtask(subtaskId1);
        inMemoryTaskManager.getSubtask(subtaskId2);
        inMemoryTaskManager.getSubtask(subtaskId3);
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println();

        inMemoryTaskManager.getTask(taskId1);
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getEpic(epicId1);
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubtask(subtaskId1);
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println();

        inMemoryTaskManager.deleteTask(taskId1);
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println();

        inMemoryTaskManager.deleteEpic(epicId1);
        System.out.println(inMemoryTaskManager.getHistory());
    }
}
