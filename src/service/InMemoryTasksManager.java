package service;

import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTasksManager implements TasksManager {

    public InMemoryTasksManager() {
        final LocalDateTime startPointOfPlanning = LocalDateTime.of(2022, 12, 1, 0, 0, 0);
        LocalDateTime begin = startPointOfPlanning;
        while (begin.isBefore(startPointOfPlanning.plusDays(365))) {
            taskGrid.put(begin, true);
            begin = begin.plusMinutes(15);
        }
    }

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();

    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    private int generatorId = 1;

    public HashMap<LocalDateTime, Boolean> taskGrid = new HashMap<>();

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    private boolean isCheckCrossing(Task task) {
        boolean isCrossing = false;
        Duration taskDuration = task.getDuration();
        LocalDateTime taskStartTime = task.getStartTime();

        // не учитывать пересечение при обновлении таски, если в обновленной версии не менялись Duration и StartTime
        if (task instanceof Subtask) {
            if (subtasks.containsKey(task.getId())) {
                Subtask oldSubtask = subtasks.get(task.getId());
                if (taskDuration.equals(oldSubtask.getDuration()) || taskStartTime.equals(oldSubtask.getStartTime())) {
                    return false;
                }
            }
        } else {
            if (tasks.containsKey(task.getId())) {
                Task oldTask = tasks.get(task.getId());
                if (taskDuration.equals(oldTask.getDuration()) || taskStartTime.equals(oldTask.getStartTime())) {
                    return false;
                }
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        LocalDateTime startPoint = task.getStartTime();

        if (task.getStartTime() != null) {
            while (startPoint.isBefore(task.getStartTime().plus(task.getDuration()))) {
                if (taskGrid.get(startPoint).equals(true)) {
                    taskGrid.put(startPoint, false);
                } else {
                    System.out.println("Задача пересекается по времени с другой задачей во временной отметке "
                            + startPoint.format(formatter));
                    isCrossing = true;
                }
                startPoint = startPoint.plusMinutes(15);
            }
        }
        return isCrossing;
    }

    @Override
    public void calculateDurationStartTimeEndTimeForEpic(Epic epic) {
        long totalDuration = 0;
        List<Task> epicSubtasks = new ArrayList<>();
        StartTimeComparator startTimeComparator = new StartTimeComparator();

        if (!epic.getSubtaskId().isEmpty()) {
            for (Integer subtasksId : epic.getSubtaskId()) {
                //часть для calculateDuration
                long subtaskDurationToMinutes = subtasks.get(subtasksId).getDuration().toMinutes();
                totalDuration = totalDuration + subtaskDurationToMinutes;

                //часть для calculateStartTimeEndTime
                epicSubtasks.add(subtasks.get(subtasksId));
            }
            epic.setDuration(Duration.ofMinutes(totalDuration));

            epicSubtasks.sort(startTimeComparator);
            epic.setStartTime(epicSubtasks.get(0).getStartTime());
            epic.setEndTime(epicSubtasks.get(epicSubtasks.size() - 1).getEndTime());
        }
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        StartTimeComparator startTimeComparator = new StartTimeComparator();
        List<Task> allTasks = new ArrayList<>();

        for (Task task : tasks.values()) {
            if (!allTasks.contains(task))
                allTasks.add(task);
        }
        for (Subtask subtask : subtasks.values()) {
            if (!allTasks.contains(subtask))
                allTasks.add(subtask);
        }

        TreeSet<Task> allTasksSet = new TreeSet<>(startTimeComparator);
        allTasksSet.addAll(allTasks);
        return allTasksSet;
    }

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
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }

        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }

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
        if (!isCheckCrossing(task)) {
            task.setId(generatorId);
            tasks.put(task.getId(), task);
            generatorId++;
            return task.getId();
        } else {
            System.out.println("Задача не добавлена");
            return -1;
        }
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
        if (!isCheckCrossing(subtask)) {
            subtask.setId(generatorId);
            subtasks.put(subtask.getId(),subtask);
            generatorId++;
            Epic epic = epics.get(subtask.getEpicId());
            epic.addSubtaskId(subtask.getId());

            if (!epic.getSubtaskId().isEmpty()) {
                calculateDurationStartTimeEndTimeForEpic(epic);
            }

            updateEpicStatus(epic.getId());
            return subtask.getId();
        } else {
            System.out.println("Задача не добавлена");
            return -1;
        }
    }

    @Override
    public void updateTask(Task newTask) {
        if (!isCheckCrossing(newTask)) {
            if (tasks.containsKey(newTask.getId())) {
                tasks.put(newTask.getId(), newTask);
            }
        } else {
            System.out.println("Задача не обновлена");
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
        if (!isCheckCrossing(newSubtask)) {
            if (subtasks.containsKey(newSubtask.getId())) {
                subtasks.put(newSubtask.getId(), newSubtask);

                Epic epic = epics.get(newSubtask.getEpicId());
                if (!epic.getSubtaskId().isEmpty()) {
                    calculateDurationStartTimeEndTimeForEpic(epic);
                }

                updateEpicStatus(newSubtask.getEpicId());
            }
        } else {
            System.out.println("Задача не обновлена");
        }
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        for (int subtaskId : epics.get(id).getSubtaskId()) {
            historyManager.remove(subtaskId);
            subtasks.remove(subtaskId);
        }

        historyManager.remove(id);

        epics.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        historyManager.remove(id);

        int epicId =  subtasks.get(id).getEpicId();
        ArrayList<Integer> subtasksId = epics.get(epicId).getSubtaskId();

        subtasksId.remove((Integer) id);

        Epic epic = epics.get(epicId);
        if (!epic.getSubtaskId().isEmpty()) {
            calculateDurationStartTimeEndTimeForEpic(epic);
        }

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

    protected void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksId = epic.getSubtaskId();
        int counterStatusNew = 0;
        int counterStatusDone = 0;
        int counterStatusInProgress = 0;

        if (subtasksId.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        for (int subtaskId : subtasksId) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStatus() == TaskStatus.NEW) {
                counterStatusNew += 1;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                counterStatusDone += 1;
            } else {
                counterStatusInProgress += 1;
            }
        }

        if (counterStatusDone == 0 && counterStatusInProgress == 0) {
            epic.setStatus(TaskStatus.NEW);
        } else if (counterStatusNew == 0 && counterStatusInProgress == 0) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
