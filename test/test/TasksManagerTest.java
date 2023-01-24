package test;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;
import service.TasksManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TasksManager>  {
    T manager;

    @Test
    public void shouldEpicIdBySubtask() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        assertEquals(manager.getSubtask(subtaskId1).getEpicId(), epicId1);
    }

    @Test
    public void shouldInProgressEpicStatusBySubtasksWithInProgressAndDoneStatus() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.IN_PROGRESS, epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.DONE, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        int subtaskId2 = manager.addNewSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpic(epicId1).getStatus());
    }

//    тесты для ArrayList<Task> getListOfTasks()
    @Test
    public void shouldStandardBehaviorGetListOfTasks() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        Task task2 = new Task("Task #2", "Description: Task #2", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);
        int taskId2 = manager.addNewTask(task2);

        ArrayList<Task> expectedListOfTasks = new ArrayList<>();
        expectedListOfTasks.add(task1);
        expectedListOfTasks.add(task2);

        assertEquals(expectedListOfTasks, manager.getListOfTasks());
    }

    @Test
    public void shouldEmptyGetListOfTasksByEmptyListOfTasks() {
        ArrayList<Task> expectedEmptyListOfTasks = new ArrayList<>();

        assertEquals(expectedEmptyListOfTasks, manager.getListOfTasks());
    }

//    тесты для ArrayList<Epic> getListOfEpics()
    @Test
    public void shouldStandardBehaviorGetListOfEpics() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        int subtaskId2 = manager.addNewSubtask(subtask2);

        ArrayList<Task> expectedListOfEpics = new ArrayList<>();
        expectedListOfEpics.add(epic1);

        assertEquals(expectedListOfEpics, manager.getListOfEpics());
    }

    @Test
    public void shouldEmptyGetListOfEpicsByEmptyListOfEpics() {
        ArrayList<Epic> expectedEmptyListOfEpics = new ArrayList<>();

        assertEquals(expectedEmptyListOfEpics, manager.getListOfEpics());
    }

//    тесты для ArrayList<Subtask> getListOfSubtasks()
    @Test
    public void shouldStandardBehaviorGetListOfSubtasks() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        int subtaskId2 = manager.addNewSubtask(subtask2);

        ArrayList<Subtask> expectedListOfSubtasks = new ArrayList<>();
        expectedListOfSubtasks.add(subtask1);
        expectedListOfSubtasks.add(subtask2);

        assertEquals(expectedListOfSubtasks, manager.getListOfSubtasks());
    }

    @Test
    public void shouldEmptyGetListOfSubtasksByEmptyListOfSubtasks() {
        ArrayList<Subtask> expectedEmptyListOfSubtasks = new ArrayList<>();

        assertEquals(expectedEmptyListOfSubtasks, manager.getListOfSubtasks());
    }

//    тесты для void deleteTasks()
    @Test
    public void shouldStandardBehaviorDeleteTasks() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        Task task2 = new Task("Task #2", "Description: Task #2", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);
        int taskId2 = manager.addNewTask(task2);
        manager.deleteTasks();

        ArrayList<Task> expectedEmptyListOfTasks = new ArrayList<>();

        assertEquals(expectedEmptyListOfTasks, manager.getListOfTasks());
    }

//    тесты для void deleteEpics()
    @Test
    public void shouldStandardBehaviorDeleteEpics() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        int subtaskId2 = manager.addNewSubtask(subtask2);
        manager.deleteEpics();

        ArrayList<Epic> expectedEmptyListOfEpics = new ArrayList<>();
        ArrayList<Subtask> expectedEmptyListOfSubtasks = new ArrayList<>();

        assertEquals(expectedEmptyListOfEpics, manager.getListOfEpics());
        assertEquals(expectedEmptyListOfSubtasks, manager.getListOfSubtasks());
    }

//    тесты для void deleteSubtasks()
    @Test
    public void shouldStandardBehaviorDeleteSubtasks() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        int subtaskId2 = manager.addNewSubtask(subtask2);
        manager.deleteSubtasks();

        ArrayList<Subtask> expectedEmptyListOfSubtasks = new ArrayList<>();
        ArrayList<Integer> expectedEmptySubtaskId = new ArrayList<>();

        assertEquals(expectedEmptyListOfSubtasks, manager.getListOfSubtasks());
        assertEquals(expectedEmptySubtaskId, manager.getEpic(epicId1).getSubtaskId());
    }

//    тесты для Task getTask(int id)
    @Test
    public void shouldStandardBehaviorGetTask() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);

        assertEquals(task1, manager.getTask(taskId1));
    }

    @Test
    public void shouldGetTaskByEmptyListOfTasks() {
        assertNull(manager.getTask(1));
    }

    @Test
    public void shouldGetTaskByTaskIncorrectId() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);

        assertNull(manager.getTask(taskId1 + 1));
    }

//    тесты для Epic getEpic(int id)
    @Test
    public void shouldStandardBehaviorGetEpic() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);

        assertEquals(epic1, manager.getEpic(epicId1));
    }

    @Test
    public void shouldGetEpicByEmptyListOfEpics() {
        assertNull(manager.getEpic(1));
    }

    @Test
    public void shouldGetEpicByEpicIncorrectId() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);

        assertNull(manager.getEpic(epicId1 + 1));
    }

//    тесты для Subtask getSubtask(int id)
    @Test
    public void shouldStandardBehaviorGetSubtask() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        assertEquals(subtask1, manager.getSubtask(subtaskId1));
    }

    @Test
    public void shouldGetSubtaskByEmptyListOfSubtasks() {
        assertNull(manager.getSubtask(1));
    }

    @Test
    public void shouldGetSubtaskBySubtaskIncorrectId() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        assertNull(manager.getSubtask(subtaskId1 + 1));
    }

//    тесты для int addNewTask(Task task)
    @Test
    public void shouldStandardBehaviorAddNewTask() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);

        assertEquals(task1, manager.getTask(taskId1));
    }

    @Test
    public void shouldAddNewTaskByEmptyListOfTasks() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);

        List<Task> tasks = manager.getListOfTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

    @Test
    public void shouldAddNewTaskByTaskIncorrectId() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);

        assertNull(manager.getTask(taskId1 + 1));
    }

//    тесты для int addNewEpic(Epic epic)
    @Test
    public void shouldStandardBehaviorAddNewEpic() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);

        assertEquals(epic1, manager.getEpic(epicId1));
    }

    @Test
    public void shouldAddNewEpicByEmptyListOfEpics() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);

        List<Epic> epics = manager.getListOfEpics();
        assertNotNull(epics);
        assertEquals(1, epics.size());
    }

    @Test
    public void shouldAddNewEpicByEpicIncorrectId() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);

        assertNull(manager.getEpic(epicId1 + 1));
    }

//    тесты для int addNewSubtask(Subtask subtask)
    @Test
    public void shouldStandardBehaviorAddNewSubtask() {                                                                ///
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        ArrayList<Integer> expectedEmptySubtaskId = new ArrayList<>();
        expectedEmptySubtaskId.add(subtaskId1);

        assertEquals(subtask1, manager.getSubtask(subtaskId1));
        assertEquals(expectedEmptySubtaskId, epic1.getSubtaskId());
    }

    @Test
    public void shouldAddNewSubtaskByEmptyListOfSubtasks() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        List<Subtask> subtasks = manager.getListOfSubtasks();
        assertNotNull(subtasks);
        assertEquals(1, subtasks.size());
    }

    @Test
    public void shouldAddNewSubtaskBySubtaskIncorrectId() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        assertNull(manager.getSubtask(subtaskId1 + 1));
    }

//    тесты для void updateTask(Task newTask)
    @Test
    public void shouldStandardBehaviorUpdateTask() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);

        Task updateTask1 = new Task("updateTask #1", "Description: updateTask #1", TaskStatus.NEW);
        updateTask1.setId(taskId1);
        manager.updateTask(updateTask1);

        assertEquals(updateTask1, manager.getTask(taskId1));
    }

    @Test
    public void shouldUpdateTaskByEmptyListOfTasks() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);

        Task updateTask1 = new Task("updateTask #1", "Description: updateTask #1", TaskStatus.NEW);
        updateTask1.setId(taskId1);
        manager.updateTask(updateTask1);

        List<Task> tasks = manager.getListOfTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

    @Test
    public void shouldUpdateTaskByTaskIncorrectId() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);

        Task updateTask1 = new Task("updateTask #1", "Description: updateTask #1", TaskStatus.NEW);
        updateTask1.setId(taskId1);
        manager.updateTask(updateTask1);

        assertNull(manager.getTask(taskId1 + 1));
    }

//    тесты для void updateEpic(Epic newEpic)
    @Test
    public void shouldStandardBehaviorUpdateEpic() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);

        Epic updateEpic1 = new Epic("updateEpic #1", "Description: updateEpic #1");
        updateEpic1.setId(epicId1);
        manager.updateEpic(updateEpic1);

        assertEquals(updateEpic1, manager.getEpic(epicId1));
    }

    @Test
    public void shouldUpdateEpicByEmptyListOfEpics() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);

        Epic updateEpic1 = new Epic("updateEpic #1", "Description: updateEpic #1");
        updateEpic1.setId(epicId1);
        manager.updateEpic(updateEpic1);

        List<Epic> epics = manager.getListOfEpics();
        assertNotNull(epics);
        assertEquals(1, epics.size());
    }

    @Test
    public void shouldUpdateEpicByEpicIncorrectId() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);

        Epic updateEpic1 = new Epic("updateEpic #1", "Description: updateEpic #1");
        updateEpic1.setId(epicId1);
        manager.updateEpic(updateEpic1);

        assertNull(manager.getEpic(epicId1 + 1));
    }

//    тесты для void updateSubtask(Subtask newSubtask)
    @Test
    public void shouldStandardBehaviorUpdateSubtask() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        Subtask updateSubtask1 = new Subtask("updateSubtask #1-e1", "Description: updateSubtask #1-e1", TaskStatus.IN_PROGRESS, epicId1);
        updateSubtask1.setId(subtaskId1);
        manager.updateSubtask(updateSubtask1);

        assertEquals(updateSubtask1, manager.getSubtask(subtaskId1));
    }

    @Test
    public void shouldUpdateSubtaskByEmptyListOfSubtasks() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        Subtask updateSubtask1 = new Subtask("updateSubtask #1-e1", "Description: updateSubtask #1-e1", TaskStatus.IN_PROGRESS, epicId1);
        updateSubtask1.setId(subtaskId1);
        manager.updateSubtask(updateSubtask1);

        List<Subtask> subtasks = manager.getListOfSubtasks();
        assertNotNull(subtasks);
        assertEquals(1, subtasks.size());
    }

    @Test
    public void shouldUpdateSubtaskBySubtaskIncorrectId() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        Subtask updateSubtask1 = new Subtask("updateSubtask #1-e1", "Description: updateSubtask #1-e1", TaskStatus.IN_PROGRESS, epicId1);
        updateSubtask1.setId(subtaskId1);
        manager.updateSubtask(updateSubtask1);

        assertNull(manager.getSubtask(subtaskId1 + 1));
    }

//    тесты для void deleteTask(int id)
    @Test
    public void shouldStandardBehaviorDeleteTask() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);
        manager.deleteTask(taskId1);

        ArrayList<Task> expectedEmptyListOfTasks = new ArrayList<>();

        assertEquals(expectedEmptyListOfTasks, manager.getListOfTasks());
    }

    @Test
    public void shouldDeleteTaskByTaskIncorrectId() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);

        ArrayList<Task> expectedEmptyListOfTasks = new ArrayList<>();
        manager.deleteTask(taskId1);

        assertNull(manager.getSubtask(taskId1 + 1));
    }

//    тесты для void deleteEpic(int id)
    @Test
    public void shouldStandardBehaviorDeleteEpic() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        manager.deleteEpic(epicId1);

        ArrayList<Epic> expectedEmptyListOfEpics = new ArrayList<>();
        ArrayList<Subtask> expectedEmptyListOfSubtasks = new ArrayList<>();

        assertEquals(expectedEmptyListOfEpics, manager.getListOfEpics());
        assertEquals(expectedEmptyListOfSubtasks, manager.getListOfSubtasks());
    }

    @Test
    public void shouldDeleteEpicByEpicIncorrectId() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        manager.deleteEpic(epicId1);

        assertNull(manager.getEpic(epicId1 + 1));
    }

//    тесты для void deleteSubtask(int id)
    @Test
    public void shouldStandardBehaviorDeleteSubtask() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        manager.deleteSubtask(subtaskId1);

        ArrayList<Subtask> expectedEmptyListOfSubtasks = new ArrayList<>();
        ArrayList<Integer> expectedEmptySubtaskId = new ArrayList<>();

        assertEquals(expectedEmptyListOfSubtasks, manager.getListOfSubtasks());
        assertEquals(expectedEmptySubtaskId, manager.getEpic(epicId1).getSubtaskId());
    }

    @Test
    public void shouldDeleteSubtaskBySubtaskIncorrectId() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        manager.deleteSubtask(subtaskId1);

        assertNull(manager.getSubtask(subtaskId1 + 1));
    }

//    тесты для ArrayList<Subtask> getSubtasksForEpic(int epicId)
    @Test
    public void shouldStandardBehaviorGetSubtasksForEpic() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.NEW, epicId1);
        int subtaskId2 = manager.addNewSubtask(subtask2);

        ArrayList<Subtask> expectedListOfSubtasks = new ArrayList<>();
        expectedListOfSubtasks.add(subtask1);
        expectedListOfSubtasks.add(subtask2);

        assertEquals(expectedListOfSubtasks, manager.getSubtasksForEpic(epicId1));
    }

    @Test
    public void shouldGetSubtasksForEpicByEmptyListOfSubtasks() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.NEW, epicId1);
        int subtaskId2 = manager.addNewSubtask(subtask2);

        List<Subtask> subtasks = manager.getListOfSubtasks();
        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
    }

    @Test
    public void shouldGetSubtasksForEpicByEmptyListOfEpics() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.NEW, epicId1);
        int subtaskId2 = manager.addNewSubtask(subtask2);

        List<Epic> epics = manager.getListOfEpics();
        assertNotNull(epics);
        assertEquals(1, epics.size());
    }

//    тесты для List<Task> getHistory()
    @Test
    public void shouldStandardBehaviorGetHistory() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        int taskId1 = manager.addNewTask(task1);
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);
        manager.getTask(taskId1);
        manager.getEpic(epicId1);
        manager.getSubtask(subtaskId1);

        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task1);
        expectedHistory.add(epic1);
        expectedHistory.add(subtask1);

        assertEquals(expectedHistory, manager.getHistory());
    }

//    тесты для новых полей duration, startTime, endTime
    @Test
    public void shouldCalculateDurationForEpicWithDuration30() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 10, 15, 00), epicId1);
        int subtaskId1 = manager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 11, 15, 00), epicId1);
        int subtaskId2 = manager.addNewSubtask(subtask2);

        manager.calculateDurationForEpic(manager.getEpic(epicId1));
        assertEquals(manager.getEpic(epicId1).getDuration().toMinutes(), 30);
    }

    @Test
    public void shouldNullCalculateDurationForEpicWithoutSubtask() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        assertNull(manager.getEpic(epicId1).getDuration());
    }

    @Test
    public void shouldNullCalculateStartTimeForEpicWithoutSubtask() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        assertNull(manager.getEpic(epicId1).getStartTime());
    }

    @Test
    public void shouldNullCalculateEndTimeForEpicWithoutSubtask() {
        Epic epic1 = new Epic("Epic #1", "Description: Epic #1");
        int epicId1 = manager.addNewEpic(epic1);
        assertNull(manager.getEpic(epicId1).getEndTime());
    }

    @Test
    public void shouldCalculateEndTimeWithDuration10() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2022, 12, 12, 12, 00));
        int taskId1 = manager.addNewTask(task1);

        LocalDateTime expectedTaskDuration = LocalDateTime.of(2022, 12, 12, 12, 10);

        assertEquals(manager.getTask(taskId1).getEndTime(), expectedTaskDuration);
    }

//    тесты для проверки пересечений и приоритизации задач
    @Test
    public void shouldEmptyPrioritizedTasksWithoutTasks() {
        assertEquals(0, manager.getPrioritizedTasks().size());
    }

    @Test
    public void shouldPrioritizedTasksWithTasks() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2022, 12, 12, 12, 00));
        int taskId1 = manager.addNewTask(task1);
        Task task2 = new Task("Task #2", "Description: Task #2", TaskStatus.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2022, 12, 13, 12, 00));
        int taskId2 = manager.addNewTask(task2);

        assertEquals(manager.getPrioritizedTasks().first(), task1);
    }

    @Test
    public void shouldIsTrueCheckCrossing() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(2022, 12, 12, 12, 00));
        int taskId1 = manager.addNewTask(task1);
        Task task2 = new Task("Task #2", "Description: Task #2", TaskStatus.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(2022, 12, 12, 12, 30));
        int taskId2 = manager.addNewTask(task2);

        assertEquals(manager.getListOfTasks().size(), 2);
    }

    @Test
    public void shouldIsFalseCheckCrossing() {
        Task task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 12, 12, 12, 00));
        int taskId1 = manager.addNewTask(task1);
        Task task2 = new Task("Task #2", "Description: Task #2", TaskStatus.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(2022, 12, 12, 12, 30));
        int taskId2 = manager.addNewTask(task2);

        assertEquals(manager.getListOfTasks().size(), 1);
    }
}
