package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.Managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static HttpTaskManager httpTaskManager = Managers.getDefaultHttpTaskManager();
    private HttpServer server;
    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson;

    public HttpTaskServer() {
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            gson = new Gson();
            server.createContext("/tasks", this::handle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handle(HttpExchange httpExchange) { // обработчик запросов, сначала делим по методу, затем по пути
        try {
            gsonBuilder.serializeNulls();
            gson = gsonBuilder.create();

            URI requestURI = httpExchange.getRequestURI();
            String path = requestURI.getPath();
            String[] splitStrings = path.split("/");
            String query = requestURI.getQuery();
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {
                case "GET": {

                    // TreeSet<Task> getPrioritizedTasks()
                    if (splitStrings[1].equals("tasks") && splitStrings.length == 2) {
                        String response = gson.toJson(httpTaskManager.getPrioritizedTasks());
                        writeResponse(httpExchange, response, 200);
                        System.out.println("Получили упорядоченный список задач");
                        return;
                    }

                    // ArrayList<Task> getListOfTasks()
                    if (splitStrings[2].equals("task") && splitStrings.length == 3 && query == null) {
                        if (httpTaskManager.getTasks().isEmpty()) {
                            writeResponse(httpExchange, "Список Task'ов пустой", 405);
                        } else {
                            String response = gson.toJson(httpTaskManager.getListOfTasks());
                            writeResponse(httpExchange, response, 200);
                            System.out.println("Получили список всех Task'ов");
                        }
                        return;
                    }

                    // ArrayList<Epic> getListOfEpics()
                    if (splitStrings[2].equals("epic") && splitStrings.length == 3 && query == null) {
                        if (httpTaskManager.getEpics().isEmpty()) {
                            writeResponse(httpExchange, "Список Epic'ов пустой", 405);
                        } else {
                            String response = gson.toJson(httpTaskManager.getListOfEpics());
                            writeResponse(httpExchange, response, 200);
                            System.out.println("Получили список всех Epic'ов");
                        }
                        return;
                    }

                    // ArrayList<Subtask> getListOfSubtasks()
                    if (splitStrings[2].equals("subtask") && splitStrings.length == 3 && query == null) {
                        if (httpTaskManager.getSubtasks().isEmpty()) {
                            writeResponse(httpExchange, "Список Subtask'ов пустой", 405);
                        } else {
                            String response = gson.toJson(httpTaskManager.getListOfSubtasks());
                            writeResponse(httpExchange, response, 200);
                            System.out.println("Получили список всех Subtask'ов");
                        }
                        return;
                    }

                    // Task getTask(int id)
                    if (splitStrings[2].equals("task") && splitStrings.length == 3 && query != null) {
                        int id = getIdFromQuery(query);
                        if (id != 0) {
                            if (httpTaskManager.getTask(id) != null) {
                                String response = gson.toJson(httpTaskManager.getTask(id));
                                writeResponse(httpExchange, response, 200);
                                System.out.println("Получили task'у по id = " + id);
                            } else {
                                writeResponse(httpExchange, "По данному id нет task'ов", 405);
                            }
                        } else {
                            writeResponse(httpExchange, "Неверно введены параметры строки запроса", 405);
                        }
                        return;
                    }

                    // Epic getEpic(int id)
                    if (splitStrings[2].equals("epic") && splitStrings.length == 3 && query != null) {
                        int id = getIdFromQuery(query);
                        if (id != 0) {
                            if (httpTaskManager.getEpic(id) != null) {
                                String response = gson.toJson(httpTaskManager.getEpic(id));
                                writeResponse(httpExchange, response, 200);
                                System.out.println("Получили epic'у по id = " + id);
                            } else {
                                writeResponse(httpExchange, "По данному id нет epic'ов", 405);
                            }
                        } else {
                            writeResponse(httpExchange, "Неверно введены параметры строки запроса", 405);
                        }
                        return;
                    }

                    // Subtask getSubtask(int id)
                    if (splitStrings[2].equals("subtask") && splitStrings.length == 3 && query != null) {
                        int id = getIdFromQuery(query);
                        if (id != 0) {
                            if (httpTaskManager.getSubtask(id) != null) {
                                String response = gson.toJson(httpTaskManager.getSubtask(id));
                                writeResponse(httpExchange, response, 200);
                                System.out.println("Получили subtask'у по id = " + id);
                            } else {
                                writeResponse(httpExchange, "По данному id нет subtask'ов", 405);
                            }
                        } else {
                            writeResponse(httpExchange, "Неверно введены параметры строки запроса", 405);
                        }
                        return;
                    }

                    // ArrayList<Subtask> getSubtasksForEpic(int epicId)
                    if (splitStrings[2].equals("subtask") && splitStrings[3].equals("epic") && splitStrings.length == 4) {
                        int id = getIdFromQuery(query);
                        if (id != 0) {
                            if (!httpTaskManager.getSubtasksForEpic(id).equals(new ArrayList<>())) {
                                String response = gson.toJson(httpTaskManager.getSubtasksForEpic(id));
                                writeResponse(httpExchange, response, 200);
                                System.out.println("Получили список subtask'ов по epic'у с id = " + id);
                            } else {
                                writeResponse(httpExchange, "У данного epic'а нет subtask'ов", 405);
                            }
                        } else {
                            writeResponse(httpExchange, "Неверно введены параметры строки запроса", 405);
                        }
                        return;
                    }

                    // List<Task> getHistory()
                    if (splitStrings[2].equals("history") && splitStrings.length == 3) {
                        String response = gson.toJson(httpTaskManager.getHistory());
                        writeResponse(httpExchange, response, 200);
                        System.out.println("Получили историю");
                        return;
                    }

                    writeResponse(httpExchange, "Неверный эндпоинт по методу GET", 405);
                    break;
                 }

                case "POST": {

                    // int addNewTask(Task task), void updateTask(Task newTask)
                    if (splitStrings[2].equals("task") && splitStrings.length == 3) {
                        InputStream taskFromBody = httpExchange.getRequestBody();
                        String body = new String(taskFromBody.readAllBytes(), StandardCharsets.UTF_8);
                        Task task;

                        try {
                            task = gson.fromJson(body, Task.class);
                            HashMap<Integer, Task> tasks = httpTaskManager.getTasks();

                            if (tasks.containsKey(task.getId())) {
                                Task oldTask = tasks.get(task.getId());
                                httpTaskManager.updateTask(task);

                                // проверяем, обновилась ли таска, выводи соответсвующее сообщение клиенту
                                Task expectedUpdateTask = tasks.get(task.getId());
                                if (oldTask.equals(expectedUpdateTask)) {
                                    writeResponse(httpExchange, "Task'а не была обновлена", 405);
                                } else {
                                    writeResponse(httpExchange, "Task'а обновлена", 200);
                                }

                            } else {
                                httpTaskManager.addNewTask(task);
                                writeResponse(httpExchange, "Task'а создана", 200);
                            }
                            return;
                        } catch (JsonSyntaxException e) {
                            writeResponse(httpExchange, "Неверный фортам JSON", 405);
                            return;
                        }
                    }

                    // int addNewEpic(Epic epic), void updateEpic(Epic newEpic)
                    if (splitStrings[2].equals("epic") && splitStrings.length == 3) {
                        InputStream epicFromBody = httpExchange.getRequestBody();
                        String body = new String(epicFromBody.readAllBytes(), StandardCharsets.UTF_8);
                        Epic epic;

                        try {
                            epic = gson.fromJson(body, Epic.class);
                            HashMap<Integer, Epic> epics = httpTaskManager.getEpics();

                            if (epics.containsKey(epic.getId())) {
                                Epic oldEpic = epics.get(epic.getId());
                                httpTaskManager.updateEpic(epic);

                                // проверяем, обновился ли эпик, выводи соответсвующее сообщение клиенту
                                Epic expectedUpdateEpic = epics.get(epic.getId());
                                if (oldEpic.equals(expectedUpdateEpic)) {
                                    writeResponse(httpExchange, "Epic не был обновлен", 405);
                                } else {
                                    writeResponse(httpExchange, "Epic обновлен", 200);
                                }

                            } else {
                                httpTaskManager.addNewEpic(epic);
                                writeResponse(httpExchange, "Epic создан", 200);
                            }
                            return;
                        } catch (JsonSyntaxException e) {
                            writeResponse(httpExchange, "Неверный фортам JSON", 405);
                            return;
                        }
                    }

                    // int addNewSubtask(Subtask subtask), void updateSubtask(Subtask newSubtask)
                    if (splitStrings[2].equals("subtask") && splitStrings.length == 3) {
                        InputStream epicFromBody = httpExchange.getRequestBody();
                        String body = new String(epicFromBody.readAllBytes(), StandardCharsets.UTF_8);
                        Subtask subtask;

                        try {
                            subtask = gson.fromJson(body, Subtask.class);
                            HashMap<Integer, Subtask> subtasks = httpTaskManager.getSubtasks();

                            if (subtasks.containsKey(subtask.getId())) {
                                Subtask oldSubtask = subtasks.get(subtask.getId());
                                httpTaskManager.updateSubtask(subtask);

                                // проверяем, обновился ли эпик, выводи соответсвующее сообщение клиенту
                                Subtask expectedUpdateSubtask = subtasks.get(subtask.getId());
                                if (oldSubtask.equals(expectedUpdateSubtask)) {
                                    writeResponse(httpExchange, "Subtask не был обновлен", 405);
                                } else {
                                    writeResponse(httpExchange, "Subtask обновлен", 200);
                                }

                            } else {
                                httpTaskManager.addNewSubtask(subtask);
                                writeResponse(httpExchange, "Subtask создан", 200);
                            }
                            return;
                        } catch (JsonSyntaxException e) {
                            writeResponse(httpExchange, "Неверный фортам JSON", 405);
                            return;
                        }
                    }

                    writeResponse(httpExchange, "Неверный эндпоинт по методу POST", 405);
                    break;
                }
                case "DELETE": {

                    // void deleteTasks()
                    if (splitStrings[2].equals("task") && splitStrings.length == 3 && query == null) {
                        if (httpTaskManager.getTasks().isEmpty()) {
                            writeResponse(httpExchange, "Удаление не выполнено, так как список Task'ов пустой", 405);
                        } else {
                            httpTaskManager.deleteTasks();
                            writeResponse(httpExchange, "Все Task'и были удалены", 200);
                        }
                        return;
                    }

                    // void deleteEpics()
                    if (splitStrings[2].equals("epic") && splitStrings.length == 3 && query == null) {
                        if (httpTaskManager.getEpics().isEmpty()) {
                            writeResponse(httpExchange, "Удаление не выполнено, так как список Epic'ов пустой", 405);
                        } else {
                            httpTaskManager.deleteEpics();
                            writeResponse(httpExchange, "Все Epic'и c их Subtask'ами были удалены", 200);
                        }
                        return;
                    }

                    // void deleteSubtasks()
                    if (splitStrings[2].equals("subtask") && splitStrings.length == 3 && query == null) {
                        if (httpTaskManager.getSubtasks().isEmpty()) {
                            writeResponse(httpExchange, "Удаление не выполнено, так как список Subtask'ов пустой", 405);
                        } else {
                            httpTaskManager.deleteSubtasks();
                            writeResponse(httpExchange, "Все Epic'и c их Subtask'ами были удалены", 200);
                        }
                        return;
                    }

                    // void deleteTask(int id)
                    if (splitStrings[2].equals("task") && splitStrings.length == 4 && query != null) {
                        int id = getIdFromQuery(query);
                        if (id != 0) {
                            if (httpTaskManager.getTask(id) != null) {
                                httpTaskManager.deleteTask(id);
                                writeResponse(httpExchange, "Task'а с " + id + " была удалена", 200);
                            } else {
                                writeResponse(httpExchange, "По данному id нет task'ов", 405);
                            }
                        } else {
                            writeResponse(httpExchange, "Неверно введены параметры строки запроса", 405);
                        }
                        return;
                    }

                    // void deleteEpic(int id)
                    if (splitStrings[2].equals("epic") && splitStrings.length == 4 && query != null) {
                        int id = getIdFromQuery(query);
                        if (id != 0) {
                            if (httpTaskManager.getEpic(id) != null) {
                                httpTaskManager.deleteEpic(id);
                                writeResponse(httpExchange, "Epic с " + id + " был удален", 200);
                            } else {
                                writeResponse(httpExchange, "По данному id нет epic'ов", 405);
                            }
                        } else {
                            writeResponse(httpExchange, "Неверно введены параметры строки запроса", 405);
                        }
                        return;
                    }
                    // void deleteSubtask(int id)
                    if (splitStrings[2].equals("subtask") && splitStrings.length == 4 && query != null) {
                        int id = getIdFromQuery(query);
                        if (id != 0) {
                            if (httpTaskManager.getSubtask(id) != null) {
                                httpTaskManager.deleteSubtask(id);
                                writeResponse(httpExchange, "Subtask'а с " + id + " была удалена", 200);
                            } else {
                                writeResponse(httpExchange, "По данному id нет subtask'ов", 405);
                            }
                        } else {
                            writeResponse(httpExchange, "Неверно введены параметры строки запроса", 405);
                        }
                        return;
                    }

                    writeResponse(httpExchange, "Неверный эндпоинт по методу DELETE", 405);
                    break;
                }
                default: {
                    System.out.println("Сервер не может обработать данный метод");
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if(responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    private int getIdFromQuery(String query) {
        int id;
        StringBuilder sb = new StringBuilder(query);
        if (!sb.substring(0, 3).equals("id=") || sb.length() != 4) {
            return 0;
        } else {
            sb.delete(0, 3);
            id = Integer.parseInt(sb.toString());;
        }
       return id;
    }

    public void startHttpTaskServer() {
        System.out.println("StartHttpTaskServer");
        server.start();
    }

    public void stopHttpTaskServer() {
        System.out.println("StopHttpTaskServer");
        server.stop(0);
    }

}