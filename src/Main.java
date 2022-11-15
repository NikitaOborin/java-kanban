import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager inMemoryTaskManager = managers.getDefault();

        Task task1 = new Task("Task #1", "Description: Task #1", Status.NEW);
        Task task2 = new Task("Task #2", "Description: Task #2", Status.NEW);
        int taskId1 = inMemoryTaskManager.addNewTask(task1);
        int taskId2 = inMemoryTaskManager.addNewTask(task2);

        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = inMemoryTaskManager.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", Status.NEW, 3);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", Status.NEW, 3);
        int subtaskId1 = inMemoryTaskManager.addNewSubtask(subtask1);
        int subtaskId2 = inMemoryTaskManager.addNewSubtask(subtask2);

        System.out.println(inMemoryTaskManager.getListOfEpics());
        System.out.println(inMemoryTaskManager.getListOfTasks());
        System.out.println(inMemoryTaskManager.getListOfSubtasks());

        System.out.println();

        inMemoryTaskManager.getTask(taskId1);
        inMemoryTaskManager.getTask(taskId2);
        inMemoryTaskManager.getEpic(epicId1);
        inMemoryTaskManager.getSubtask(subtaskId1);
        inMemoryTaskManager.getSubtask(subtaskId2);

        inMemoryTaskManager.getTask(taskId1);
        inMemoryTaskManager.getTask(taskId2);
        inMemoryTaskManager.getEpic(epicId1);
        inMemoryTaskManager.getSubtask(subtaskId1);
        inMemoryTaskManager.getSubtask(subtaskId2);

        System.out.println(Managers.getDefaultHistory().getHistory());

    }
}
