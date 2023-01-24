package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TasksManager {
    ArrayList<Task> getListOfTasks();

    ArrayList<Epic> getListOfEpics();

    ArrayList<Subtask> getListOfSubtasks();

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    int addNewSubtask(Subtask subtask);

    void updateTask(Task newTask);

    void updateEpic(Epic newEpic);

    void updateSubtask(Subtask newSubtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    ArrayList<Subtask> getSubtasksForEpic(int epicId);

    List<Task> getHistory();

    void calculateDurationForEpic(Epic epic);

    void calculateStartTimeForEpic(Epic epic);

    void calculateEndTimeForEpic(Epic epic);

    TreeSet<Task> getPrioritizedTasks();
}
