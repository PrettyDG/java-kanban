package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import models.DefaultTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private static TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        TasksHandler.taskManager = taskManager;
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
                writeToUser(exchange, taskManager.getDefaultTaskByID(id).toString());
                return taskManager.getDefaultTaskByID(id);
            } else {
                exchange.sendResponseHeaders(404, 0);
                writeToUser(exchange, "Ошибка 404, DefaultTask с ID:" + id + " не найден");
                return null;
            }
        } else {
            exchange.sendResponseHeaders(200, 0);
            writeToUser(exchange, taskManager.getAllDefaultTasks().toString());
            return taskManager.getAllDefaultTasks();
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
            DefaultTask defaultTask = gson.fromJson(bodyTask, DefaultTask.class);
            defaultTask.setId(id);
            if (taskManager.updateDefaultTask(id, defaultTask) != null) {
                exchange.sendResponseHeaders(201, 0);
                writeToUser(exchange, "Задача создана - " + defaultTask);
            } else {
                exchange.sendResponseHeaders(406, 0);
                writeToUser(exchange, "Ошибка 406, Задача пересекается с другой");
            }
        } else {
            DefaultTask defaultTask = gson.fromJson(bodyTask, DefaultTask.class);
            if (taskManager.createDefaultTask(defaultTask) != null) {
                exchange.sendResponseHeaders(201, 0);
                writeToUser(exchange, "Задача создана - " + defaultTask.toString());
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

        taskManager.deleteDefaultTaskByID(id);
        exchange.sendResponseHeaders(200, 0);
        writeToUser(exchange, "DefaultTask - " + id + ", была успешно удалена");
    }
}
