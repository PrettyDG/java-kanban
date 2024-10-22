package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.Managers;

import java.io.IOException;
import java.io.OutputStream;

public abstract class BaseHttpHandler implements HttpHandler {

    protected static Gson gson = Managers.getGson();

    public static void writeToUser(HttpExchange exchange, String string) {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(string.getBytes());
        } catch (IOException e) {
            System.out.println(e);
        }
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


    public abstract Object handleGet(HttpExchange exchange) throws IOException;

    public abstract void handlePost(HttpExchange exchange) throws IOException;

    public abstract void handleDelete(HttpExchange exchange) throws IOException;
}