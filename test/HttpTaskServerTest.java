import com.google.gson.Gson;
import controllers.InMemoryTaskManager;
import controllers.Managers;
import http.HttpTaskServer;
import models.DefaultTask;
import models.Epic;
import models.Subtask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TaskStage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HttpTaskServerTest {

    // создаём экземпляр InMemoryTaskManager
    InMemoryTaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = Managers.getGson();

    public HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllDefaultTasks();
        manager.deleteAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        DefaultTask defaultTask1 = new DefaultTask(0, "Task1", "Task1 description"
                , TaskStage.NEW, LocalDateTime.of(2024, 10, 4, 19, 0), Duration.ofMinutes(30));

        String taskJson = gson.toJson(defaultTask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        System.out.println(taskJson);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());

        ArrayList<DefaultTask> tasksFromManager = manager.getAllDefaultTasks();

        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals("Task1", tasksFromManager.get(0).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "Epic1 description");
        manager.createEpicTask(epic);
        Subtask subtask = new Subtask(1, "Subtask1", "Subtask1 description", TaskStage.NEW, 0
                , LocalDateTime.of(2022, 10, 4, 19, 0), Duration.ofMinutes(30));

        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());

        ArrayList<Subtask> tasksFromManager = manager.getAllSubtasks();

        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals("Subtask1", tasksFromManager.get(0).getTaskName(), "Некорректное имя задачи");
    }


    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "Epic1 description", LocalDateTime.now(), Duration.ofMinutes(30));

        String taskJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());

        ArrayList<Epic> tasksFromManager = manager.getEpicTasks();

        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals("Epic1", tasksFromManager.get(0).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void deleteDefaultTask() throws IOException, InterruptedException {
        DefaultTask defaultTask1 = new DefaultTask(0, "Task1", "Task1 description"
                , TaskStage.NEW, LocalDateTime.of(2024, 2, 4, 19, 0), Duration.ofMinutes(30));
        manager.createDefaultTask(defaultTask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/0");

        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertNull(manager.getDefaultTaskByID(0));
    }

    @Test
    public void getPrioritized() throws IOException, InterruptedException {
        DefaultTask defaultTask1 = new DefaultTask(0, "Task1", "Task1 description"
                , TaskStage.NEW, LocalDateTime.of(2024, 2, 4, 19, 0), Duration.ofMinutes(30));

        DefaultTask defaultTask2 = new DefaultTask(1, "Task2", "Task2 description"
                , TaskStage.NEW, LocalDateTime.of(2021, 2, 4, 19, 0), Duration.ofMinutes(30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        DefaultTask defaultTask1 = new DefaultTask(0, "Task1", "Task1 description"
                , TaskStage.NEW, LocalDateTime.of(2024, 2, 4, 19, 0), Duration.ofMinutes(30));

        DefaultTask defaultTask2 = new DefaultTask(1, "Task2", "Task2 description"
                , TaskStage.NEW, LocalDateTime.of(2021, 2, 4, 19, 0), Duration.ofMinutes(30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}