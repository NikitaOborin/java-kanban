package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected int generatorId = 1;

    public ArrayList<Task> getListOfTasks() { // пункт ТЗ 2.1
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getListOfEpics() { // пункт ТЗ 2.1
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getListOfSubtasks() { // пункт ТЗ 2.1
        return new ArrayList<>(subtasks.values());
    }

    public void deleteTasks() { // пункт ТЗ 2.2
        tasks.clear();
        subtasks.clear();
    }

    public void deleteEpics() { // пункт ТЗ 2.2
        epics.clear();
    }

    public void deleteSubtasks() { // пункт ТЗ 2.2
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().clear();
        }
    }

    public Task getTask(int id) { // пункт ТЗ 2.3
        return tasks.get(id);
    }

    public Epic getEpic(int id) { // пункт ТЗ 2.3
        return epics.get(id);
    }

    public Subtask getSubtask(int id) { // пункт ТЗ 2.3
        return subtasks.get(id);
    }

    public void addNewTask(Task task) { // пункт ТЗ 2.4
        task.setId(generatorId);
        tasks.put(task.getId(), task);
        generatorId++;
    }

    public void addNewEpic(Epic epic) { // пункт ТЗ 2.4
        epic.setId(generatorId);
        epics.put(epic.getId(), epic);
        generatorId++;

        updateEpicStatus(epic.getId());
    }

    public void addNewSubtask(Subtask subtask) { // пункт ТЗ 2.4
        subtask.setId(generatorId);
        subtasks.put(subtask.getId(),subtask);
        generatorId++;
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(subtask.getId());
    }

    public void updateTask(Task newTask) { // пункт ТЗ 2.5
        if (!tasks.containsKey(newTask.getId())) {
            System.out.println("Такой задачи не существует!");
        } else {
            tasks.put(newTask.getId(), newTask);
        }

    }

    public void updateEpic(Epic newEpic) { // пункт ТЗ 2.5
        if (!epics.containsKey(newEpic.getId())) {
            System.out.println("Такого эпика не существует!");
        } else {
            epics.put(newEpic.getId(), newEpic);
            updateEpicStatus(newEpic.getId());
        }
    }

    public void updateSubtask(Subtask newSubtask) { // пункт ТЗ 2.5
        if (!subtasks.containsKey(newSubtask.getId())) {
            System.out.println("Такой подзадачи не существует!");
        } else {
            subtasks.put(newSubtask.getId(), newSubtask);
            updateEpicStatus(newSubtask.getEpicId());
        }
    }

    public void deleteTask(int id) { // пункт ТЗ 2.6
        tasks.remove(id);
    }

    public void deleteEpic(int id) { // пункт ТЗ 2.6
        for (int subtaskId : epics.get(id).getSubtaskId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public void deleteSubtask(int id) { // пункт ТЗ 2.6
        int epicId =  subtasks.get(id).getEpicId();
        ArrayList<Integer> subtaskId = epics.get(epicId).getSubtaskId();

        subtaskId.remove(id);
        subtasks.remove(id);
    }

    public ArrayList<Subtask> getSubtasksForEpic(int epicId) { // пункт ТЗ 3.1
        ArrayList<Subtask> listOfSubtasksForEpic = new ArrayList<>();
        ArrayList<Integer> subtasksId = epics.get(epicId).getSubtaskId();
        for (int subtaskId : subtasksId) {
            listOfSubtasksForEpic.add(subtasks.get(subtaskId));
        }
        return listOfSubtasksForEpic;
    }

    private void updateEpicStatus(int epicId) { // пункт ТЗ 4.2
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksId = epic.getSubtaskId();
        int counterStatusNew = 0;
        int counterStatusDone = 0;
        int counterStatusInProgress = 0;

        if (subtasksId.isEmpty()) {
            epic.setStatus("NEW");
            return;
        }

        for (int subtaskId : subtasksId) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStatus().equals("NEW")) {
                counterStatusNew += 1;
            } else if (subtask.getStatus().equals("DONE")) {
                counterStatusDone += 1;
            } else {
                counterStatusInProgress += 1;
            }
        }

        if (counterStatusDone == 0 && counterStatusInProgress == 0) {
            epic.setStatus("NEW");
        } else if (counterStatusNew == 0 && counterStatusInProgress == 0) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }
}
