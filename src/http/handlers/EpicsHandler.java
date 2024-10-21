package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import models.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private static TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
        EpicsHandler.taskManager = taskManager;
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
            if (taskManager.getTaskforUserByID(id) == null) {
                exchange.sendResponseHeaders(404, 0);
                writeToUser(exchange, "Ошибка 404, Epic с ID:" + id + " не найден");
                return null;
            } else if (splitPath.length > 3) {
                exchange.sendResponseHeaders(200, 0);
                writeToUser(exchange, taskManager.getAllSubtasksByEpicID(id).toString());
                return taskManager.getAllSubtasksByEpicID(id);
            } else {
                exchange.sendResponseHeaders(200, 0);
                writeToUser(exchange, taskManager.getEpicTaskByID(id).toString());
                return taskManager.getEpicTaskByID(id);
            }
        } else {
            exchange.sendResponseHeaders(200, 0);
            writeToUser(exchange, taskManager.getEpicTasks().toString());
            return taskManager.getEpicTasks();
        }
    }

    public static void handlePost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        InputStream inputStream = exchange.getRequestBody();
        String bodyTask = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Epic epic = gson.fromJson(bodyTask, Epic.class);
        taskManager.createEpicTask(epic);
        exchange.sendResponseHeaders(201, 0);
        writeToUser(exchange, "Задача создана - " + epic.toString());
    }

    public static void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        Integer id = Integer.parseInt(splitPath[2]);

        taskManager.deleteEpicTask(id);
        exchange.sendResponseHeaders(200, 0);
        writeToUser(exchange, "Epic - " + id + ", была успешно удалена");
    }
}
