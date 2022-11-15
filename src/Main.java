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
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

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

        System.out.println(inMemoryHistoryManager.getHistory());

//        // изменим статусы уже созданных объектов, с указанием id конкретного объекта
//        Task task = inMemoryTaskManager.getTask(taskId1);
//        task.setStatus(Status.IN_PROGRESS);
//        inMemoryTaskManager.updateTask(task);
//
//        task = inMemoryTaskManager.getTask(taskId2);
//        task.setStatus(Status.IN_PROGRESS);
//        inMemoryTaskManager.updateTask(task);
//
//        Subtask subtask = inMemoryTaskManager.getSubtask(subtaskId1);
//        subtask.setStatus(Status.IN_PROGRESS);
//        inMemoryTaskManager.updateSubtask(subtask);
//
//        subtask = inMemoryTaskManager.getSubtask(subtaskId2);
//        subtask.setStatus(Status.IN_PROGRESS);
//        inMemoryTaskManager.updateSubtask(subtask);
//
//        System.out.println();
//        System.out.println(inMemoryTaskManager.getListOfEpics());
//        System.out.println(inMemoryTaskManager.getListOfTasks());
//        System.out.println(inMemoryTaskManager.getListOfSubtasks());
//
//        // удалим одну из задач и один из эпиков
//        inMemoryTaskManager.deleteTask(taskId1);
//        inMemoryTaskManager.deleteEpic(epicId1);
    }
}
