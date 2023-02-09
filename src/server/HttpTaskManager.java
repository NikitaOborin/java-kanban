package server;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;

import service.FileBackedTasksManager;
import service.Managers;

import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTasksManager {

    KVTaskClient kvTaskClient;
    private Gson gson;

    public HttpTaskManager(String URL) {
        super();
        kvTaskClient = new KVTaskClient(URL);
        load();
    }

    @Override
    protected void save() {
        gson = Managers.getGson();
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        String jsonSubtasks = gson.toJson(new ArrayList<>(subtasks.values()));
        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        String jsonHistory = gson.toJson(new ArrayList<>(historyManager.getHistory()));

        kvTaskClient.put("tasks", jsonTasks);
        kvTaskClient.put("subtasks", jsonSubtasks);
        kvTaskClient.put("epics", jsonEpics);
        kvTaskClient.put("history", jsonHistory);
    }

    private void load() {
        gson = Managers.getGson();
        ArrayList<Task> tasks = gson.fromJson(kvTaskClient.load("tasks"),new TypeToken<ArrayList<Task>>() {}.getType());
        if (tasks != null) {
            for (Task task : tasks) {
                this.tasks.put(task.getId(), task);
            }
        }
        ArrayList<Subtask> subtasks = gson.fromJson(kvTaskClient.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {}.getType());
        if (subtasks != null) {
            for (Subtask subtask : subtasks) {
                this.subtasks.put(subtask.getId(), subtask);
            }
        }

        ArrayList<Epic> epics = gson.fromJson(kvTaskClient.load("epics"), new TypeToken<ArrayList<Epic>>() {}.getType());
        if (epics != null) {
            for (Epic epic : epics) {
                this.epics.put(epic.getId(), epic);
            }
        }

        ArrayList<Task> history = gson.fromJson(kvTaskClient.load("history"), new TypeToken<ArrayList<Task>>() {}.getType());
        if (history != null) {
            for (Task task : history) {
                this.historyManager.add(task);
            }
        }
    }
}
