package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    private static TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        HistoryHandler.taskManager = taskManager;
    }

    @Override
    public Object handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");

        exchange.sendResponseHeaders(200, 0);
        writeToUser(exchange, taskManager.getHistory().toString());

        return taskManager.getHistory();
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
