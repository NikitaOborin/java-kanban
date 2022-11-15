package service;

import model.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    public ArrayList<Task> viewedTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (viewedTasks.size() < 10) {
            viewedTasks.add(task);
        } else {
            viewedTasks.remove(0);
            viewedTasks.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return viewedTasks;
    }
}

