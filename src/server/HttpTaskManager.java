package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import model.Subtask;
import model.Task;

import service.FileBackedTasksManager;

import java.util.HashMap;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private final String URL;
    KVTaskClient kvTaskClient;

    public HttpTaskManager(String URL) {
        super("src\\\\docs\\\\file.csv");
        this.URL = URL;
        kvTaskClient = new KVTaskClient(URL);
    }

    private String getJsonForTask(HashMap<Integer, Task> tasks) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        return gson.toJson(tasks);
    }

    private String getJsonForEpic(HashMap<Integer, Epic> epics) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        return gson.toJson(epics);
    }

    private String getJsonForSubtask(HashMap<Integer, Subtask> subtasks) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        return gson.toJson(subtasks);
    }

    private void saveOnKVServer() {
        kvTaskClient.put("Tasks", getJsonForTask(tasks));
        kvTaskClient.put("Epic", getJsonForEpic(epics));
        kvTaskClient.put("Subtask", getJsonForSubtask(subtasks));
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        saveOnKVServer();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        saveOnKVServer();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        saveOnKVServer();
    }

    @Override
    public int addNewTask(Task task) {
        super.addNewTask(task);
        saveOnKVServer();
        return task.getId();
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        saveOnKVServer();
        return subtask.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        saveOnKVServer();
        return epic.getId();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        saveOnKVServer();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        saveOnKVServer();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        saveOnKVServer();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        saveOnKVServer();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        saveOnKVServer();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        saveOnKVServer();
    }

    @Override
    public void updateEpicStatus(int epicId) {
        super.updateEpicStatus(epicId);
        saveOnKVServer();
    }

    @Override
    public List<Task> getHistory() {
        super.getHistory();
        saveOnKVServer();
        return getHistoryManager().getHistory();
    }
}
