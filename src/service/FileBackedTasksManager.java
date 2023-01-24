package service;

import exeption.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager {

    protected final Path file;
    private static final String HEAD = "id,type,name,status,description,epic";

    public FileBackedTasksManager(String path) {
        super();
        file = Paths.get(path);
    }

    public Path getFile() {
        return file;
    }

    private void save() {
        try {
            if (Files.exists(file)) {
                Files.delete(file);
            }
            Files.createFile(file);
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл для записи");
        }

        try (Writer fileWriter = new FileWriter(file.toFile())) {
            fileWriter.write(HEAD);
            fileWriter.write("\n");

            for (Task task : getListOfTasks()) {
                fileWriter.write(task.toString() + "\n");
            }

            for (Epic epic : getListOfEpics()) {
                fileWriter.write(epic.toString() + "\n");
            }

            for (Subtask subtask : getListOfSubtasks()) {
                fileWriter.write(subtask.toString() + "\n");
            }

            fileWriter.write("\n");
            fileWriter.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл");
        }
    }

    public static FileBackedTasksManager loadFromFile(Path file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file.toString());
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file.toFile()))) {
            while (fileReader.ready()) {
                String line = fileReader.readLine();

                if (line.equals("")) {
                    break;
                }

                if (line.equals(HEAD)) {
                    continue;
                }

                Task task = fromString(line);
                fileBackedTasksManager.addTask(task);
            }

            String lineHistory = fileReader.readLine();

            if (lineHistory != null) {
                for (int id : historyFromString(lineHistory)) {
                    if (fileBackedTasksManager.tasks.containsKey(id)) {
                        Task task = fileBackedTasksManager.tasks.get(id);
                        fileBackedTasksManager.getHistoryManager().add(task);
                    } else if (fileBackedTasksManager.epics.containsKey(id)) {
                        Epic epic = fileBackedTasksManager.epics.get(id);
                        fileBackedTasksManager.getHistoryManager().add(epic);
                    } else if (fileBackedTasksManager.subtasks.containsKey(id)) {
                        Subtask subtask = fileBackedTasksManager.subtasks.get(id);
                        fileBackedTasksManager.getHistoryManager().add(subtask);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла");
        }
        return fileBackedTasksManager;
    }

    public static Task fromString(String value) {
        String[] valueArray = value.split(",");

        final int id = Integer.parseInt(valueArray[0]);
        final TaskType type = TaskType.valueOf(valueArray[1]);
        final String name = valueArray[2];
        final TaskStatus status = TaskStatus.valueOf(valueArray[3]);
        final String description = valueArray[4];

        if (TaskType.TASK.equals(type)) {
            Task task = new Task(name, description, status);
            task.setId(id);
            return task;
        } else if (TaskType.EPIC.equals(type)) {
            Task epic = new Epic(name, description, status);
            epic.setId(id);
            return epic;
        } else {
            Task subtask = new Subtask(name, description, status, Integer.parseInt(valueArray[5]));
            subtask.setId(id);
            return subtask;
        }
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();

        if (!history.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Task task : history) {
                sb.append(task.getId());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
        return "";
    }

    public static List<Integer> historyFromString(String value) {
        String[] valueArray = value.split(",");
        List<Integer> historyId = new ArrayList<>();
        for (String s : valueArray) {
            historyId.add(Integer.parseInt(s));
        }
        return historyId;
    }

    private void addTask(Task task) {
        if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask) {
            subtasks.put(task.getId(), (Subtask) task);
        } else {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public int addNewTask(Task task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void updateEpicStatus(int epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public List<Task> getHistory() {
        super.getHistory();
        save();
        return getHistoryManager().getHistory();
    }
}
