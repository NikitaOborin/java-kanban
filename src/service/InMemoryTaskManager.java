package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected LinkedList<Task> history = new LinkedList<>();
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int generatorId = 1;

    @Override
    public ArrayList<Task> getListOfTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getListOfEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getListOfSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().clear();
        }
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public int addNewTask(Task task) {
        task.setId(generatorId);
        tasks.put(task.getId(), task);
        generatorId++;
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(generatorId);
        epics.put(epic.getId(), epic);
        generatorId++;

        updateEpicStatus(epic.getId());
        return epic.getId();
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        subtask.setId(generatorId);
        subtasks.put(subtask.getId(),subtask);
        generatorId++;
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(subtask.getId());
        return subtask.getId();
    }

    @Override
    public void updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getId())) {
            tasks.put(newTask.getId(), newTask);
        }
    }

    @Override
    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getId())) {
            epics.put(newEpic.getId(), newEpic);
            updateEpicStatus(newEpic.getId());
        }
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.getId())) {
            subtasks.put(newSubtask.getId(), newSubtask);
            updateEpicStatus(newSubtask.getEpicId());
        }
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        for (int subtaskId : epics.get(id).getSubtaskId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        int epicId =  subtasks.get(id).getEpicId();
        ArrayList<Integer> subtasksId = epics.get(epicId).getSubtaskId();

        subtasksId.remove(id);
        subtasks.remove(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasksForEpic(int epicId) {
        ArrayList<Subtask> listOfSubtasksForEpic = new ArrayList<>();
        ArrayList<Integer> subtasksId = epics.get(epicId).getSubtaskId();
        for (int subtaskId : subtasksId) {
            listOfSubtasksForEpic.add(subtasks.get(subtaskId));
        }
        return listOfSubtasksForEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksId = epic.getSubtaskId();
        int counterStatusNew = 0;
        int counterStatusDone = 0;
        int counterStatusInProgress = 0;

        if (subtasksId.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        for (int subtaskId : subtasksId) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStatus() == Status.NEW) {
                counterStatusNew += 1;
            } else if (subtask.getStatus() == Status.DONE) {
                counterStatusDone += 1;
            } else {
                counterStatusInProgress += 1;
            }
        }

        if (counterStatusDone == 0 && counterStatusInProgress == 0) {
            epic.setStatus(Status.NEW);
        } else if (counterStatusNew == 0 && counterStatusInProgress == 0) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

}
