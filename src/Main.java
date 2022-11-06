import model.Epic;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Task #1", "Description: Task #1", "NEW");
        Task task2 = new Task("Task #2", "Description: Task #2", "NEW");
        taskManager.addNewTask(task1); // id 1
        taskManager.addNewTask(task2); // id 2

        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        taskManager.addNewEpic(epic1); // id 3

        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Epic #1-e1", "NEW", 3);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Epic #2-e1", "NEW", 3);
        taskManager.addNewSubtask(subtask1); // id 4
        taskManager.addNewSubtask(subtask2); // id 5

        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfTasks());
        System.out.println(taskManager.getListOfSubtasks());

        // изменим статусы уже созданных объектов, с указанием id конкретного объекта
        task1 = new Task(1, "updateTask #1", "Description: updateTask #1", "IN_PROGRESS");
        task2 = new Task(2, "updateTask #2", "Description: updateTask #2", "IN_PROGRESS");
        taskManager.updateTask(task1);
        taskManager.updateTask(task2);

        subtask1 = new Subtask(4, "updateSubtask #1-e1", "Description: updateEpic #1-e1", "IN_PROGRESS", 3);
        subtask2 = new Subtask(5, "updateSubtask #2-e1", "Description: updateEpic #2-e1", "IN_PROGRESS", 3);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        System.out.println();
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfTasks());
        System.out.println(taskManager.getListOfSubtasks());

        // удалим одну из задач и один из эпиков
        taskManager.deleteTask(1);
        taskManager.deleteEpic(3);
    }
}
