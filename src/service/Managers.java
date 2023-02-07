package service;

import server.HttpTaskManager;

import java.nio.file.Path;

public class Managers {

    public static TasksManager getDefault() {
        return new InMemoryTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileManager(Path file) {
        return new FileBackedTasksManager(file.toString());
    }

    public static HttpTaskManager getDefaultHttpTaskManager() {
        return new HttpTaskManager("http://localhost:8078");
    }
}