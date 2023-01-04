import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;
import service.FileBackedTasksManager;

public class Main {

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("src\\docs\\file.csv");

        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        Task task2 = new Task("Task #2", "Description: Task #2", TaskStatus.NEW);
        int taskId1 = fileBackedTasksManager.addNewTask(task1);
        int taskId2 = fileBackedTasksManager.addNewTask(task2);

        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        Epic epic2 = new Epic("Epic #2", "Description: Epic #2");
        int epicId1 = fileBackedTasksManager.addNewEpic(epic1);
        int epicId2 = fileBackedTasksManager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, 3);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.NEW, 3);
        Subtask subtask3 = new Subtask("Subtask #3-e1", "Description: Subtask #3-e1", TaskStatus.NEW, 3);
        int subtaskId1 = fileBackedTasksManager.addNewSubtask(subtask1);
        int subtaskId2 = fileBackedTasksManager.addNewSubtask(subtask2);
        int subtaskId3 = fileBackedTasksManager.addNewSubtask(subtask3);

        fileBackedTasksManager.getTask(taskId1);
        fileBackedTasksManager.getEpic(epicId1);
        fileBackedTasksManager.getSubtask(subtaskId1);
        System.out.println(fileBackedTasksManager.getHistory());
        System.out.println();

        FileBackedTasksManager.loadFromFile(fileBackedTasksManager.getFile());
    }
}
