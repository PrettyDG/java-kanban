package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private static TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        PrioritizedHandler.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handleGet(exchange);
                break;
            default:
                writeToUser(exchange, "Данный метод не предусмотрен");
        }
    }

    public static Object handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");

        exchange.sendResponseHeaders(200, 0);
        writeToUser(exchange, taskManager.getPrioritizedTasks().toString());

        return taskManager.getPrioritizedTasks();
    }
}
