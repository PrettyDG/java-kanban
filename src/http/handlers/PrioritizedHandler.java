package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {
    private static TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        PrioritizedHandler.taskManager = taskManager;
    }

    @Override
    public Object handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");

        exchange.sendResponseHeaders(200, 0);
        writeToUser(exchange, taskManager.getPrioritizedTasks().toString());

        return taskManager.getPrioritizedTasks();
    }

    @Override
    public void handlePost(HttpExchange exchange) throws IOException {
        writeToUser(exchange, "Данный метод не предусмотрен");
    }

    @Override
    public void handleDelete(HttpExchange exchange) throws IOException {
        writeToUser(exchange, "Данный метод не предусмотрен");
    }
}
