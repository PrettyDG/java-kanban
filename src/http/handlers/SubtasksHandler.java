package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import models.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private static TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handleGet(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
            case "DELETE":
                handleDelete(exchange);
                break;
            default:
                writeToUser(exchange, "Данный метод не предусмотрен");
        }
    }

    public static Object handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        Integer id = null;
        if (splitPath.length > 2) {
            id = Integer.parseInt(splitPath[2]);
        }
        if (id != null) {
            if (taskManager.getTaskforUserByID(id) != null) {
                exchange.sendResponseHeaders(200, 0);
                writeToUser(exchange, taskManager.getSubtaskByID(id).toString());
                return taskManager.getSubtaskByID(id);
            } else {
                exchange.sendResponseHeaders(404, 0);
                writeToUser(exchange, "Ошибка 404, Subtask с ID:" + id + " не найден");
                return null;
            }
        } else {
            exchange.sendResponseHeaders(200, 0);
            writeToUser(exchange, taskManager.getAllSubtasks().toString());
            return gson.toJson(taskManager.getAllSubtasks());
        }
    }

    public static void handlePost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        Integer id = null;
        if (splitPath.length > 2) {
            id = Integer.parseInt(splitPath[2]);
        }
        InputStream inputStream = exchange.getRequestBody();
        String bodyTask = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);


        if (id != null) {
            Subtask subtask = gson.fromJson(bodyTask, Subtask.class);
            if (taskManager.updateSubtask(id, subtask, taskManager.getEpicTaskByID(subtask.getEpicID())) != null) {
                exchange.sendResponseHeaders(201, 0);
                writeToUser(exchange, "Задача создана - " + subtask.toString());
            } else {
                exchange.sendResponseHeaders(406, 0);
                writeToUser(exchange, "Ошибка 406, Задача пересекается с другой");
            }
        } else {
            Subtask subtask = gson.fromJson(bodyTask, Subtask.class);
            if (taskManager.createSubtask(subtask, taskManager.getEpicTaskByID(subtask.getEpicID())) != null) {
                exchange.sendResponseHeaders(201, 0);
                writeToUser(exchange, "Задача создана - " + subtask.toString());
            } else {
                exchange.sendResponseHeaders(406, 0);
                writeToUser(exchange, "Ошибка 406, Задача пересекается с другой");
            }
        }
    }

    public static void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        Integer id = Integer.parseInt(splitPath[2]);

        taskManager.deleteSubtaskByID(id);
        exchange.sendResponseHeaders(200, 0);
        writeToUser(exchange, "Subtask - " + id + ", была успешно удалена");
    }
}