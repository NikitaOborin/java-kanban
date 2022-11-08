import model.Epic;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Task #1", "Description: Task #1", "NEW");
        Task task2 = new Task("Task #2", "Description: Task #2", "NEW");
        int taskId1 = taskManager.addNewTask(task1);
        int taskId2 = taskManager.addNewTask(task2);

        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = taskManager.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Epic #1-e1", "NEW", 3);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Epic #2-e1", "NEW", 3);
        int subtaskId1 = taskManager.addNewSubtask(subtask1);
        int subtaskId2 = taskManager.addNewSubtask(subtask2);

        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfTasks());
        System.out.println(taskManager.getListOfSubtasks());

        // изменим статусы уже созданных объектов, с указанием id конкретного объекта
        Task task = taskManager.getTask(taskId1);
        task.setStatus("IN_PROGRESS");
        taskManager.updateTask(task);

        task = taskManager.getTask(taskId2);
        task.setStatus("IN_PROGRESS");
        taskManager.updateTask(task);

        Subtask subtask = taskManager.getSubtask(subtaskId1);
        subtask.setStatus("IN_PROGRESS");
        taskManager.updateSubtask(subtask);

        subtask = taskManager.getSubtask(subtaskId2);
        subtask.setStatus("IN_PROGRESS");
        taskManager.updateSubtask(subtask);

        System.out.println();
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfTasks());
        System.out.println(taskManager.getListOfSubtasks());

        // удалим одну из задач и один из эпиков
        taskManager.deleteTask(taskId1);
        taskManager.deleteEpic(epicId1);
    }
}
