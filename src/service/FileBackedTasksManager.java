package service;

import model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTasksManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

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
            System.out.println("Не удалось найти файл для записи");
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
            System.out.println("Не удалось сохранить в файл");
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

                String[] valueArray = line.split(",");

                if (Objects.equals(valueArray[1], "EPIC")) {
                    Epic epic = (Epic) fromString(line);
                    fileBackedTasksManager.addEpicFromFile(epic);
                } else if (Objects.equals(valueArray[1], "SUBTASK")) {
                    Subtask subtask = (Subtask) fromString(line);
                    fileBackedTasksManager.addSubtaskFromFile(subtask);
                } else if (Objects.equals(valueArray[1], "TASK")) {
                    Task task = fromString(line);
                    fileBackedTasksManager.addTaskFromFile(task);
                }
            }

            String lineHistory = fileReader.readLine();
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

        } catch (IOException e) {
            System.out.println("Не удалось считать данные из файла");
        }

        return fileBackedTasksManager;
    }

    public static Task fromString(String value) {
        String[] valueArray = value.split(",");
        if (Objects.equals(valueArray[1], TypeOfTasks.TASK.toString())) {
            Task task = new Task(valueArray[2], valueArray[4], Status.valueOf(valueArray[3]));
            task.setId(Integer.parseInt(valueArray[0]));
            return task;
        } else if (Objects.equals(valueArray[1], TypeOfTasks.EPIC.toString())) {
            Task epic = new Epic(valueArray[2], valueArray[4], Status.valueOf(valueArray[3]));
            epic.setId(Integer.parseInt(valueArray[0]));
            return epic;
        } else {
            Task subtask = new Subtask(valueArray[2], valueArray[4], Status.valueOf(valueArray[3]), Integer.parseInt(valueArray[5]));
            subtask.setId(Integer.parseInt(valueArray[0]));
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

    public void addTaskFromFile(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addSubtaskFromFile(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void addEpicFromFile(Epic epic) {
        epics.put(epic.getId(), epic);
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
