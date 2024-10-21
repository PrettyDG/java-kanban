import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import http.HttpTaskServer;
import models.DefaultTask;
import models.Epic;
import models.Subtask;
import models.Task;
import utils.TaskStage;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        HttpTaskServer httpTaskServer;
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        DefaultTask defaultTask1 = new DefaultTask(0, "Task1", "Task1 description",
                TaskStage.NEW, LocalDateTime.of(2024, 10, 4, 19, 0), Duration.ofMinutes(30));
        defaultTask1.setStage(TaskStage.NEW);
        inMemoryTaskManager.createDefaultTask(defaultTask1);

        Epic epic1 = new Epic(1, "Epic1", "Epic1 description", TaskStage.NEW);
        inMemoryTaskManager.createEpicTask(epic1);

        Subtask subtask1 = new Subtask(1, "Subtask2", "Subtask2 description3",
                TaskStage.NEW, 0, LocalDateTime.now().minusHours(5), Duration.ofMinutes(30));
        inMemoryTaskManager.createSubtask(subtask1, epic1);

        try {
            httpTaskServer = new HttpTaskServer(inMemoryTaskManager);
            httpTaskServer.start();
            System.out.println("HTTP-сервер запущен");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllDefaultTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpicTasks()) {
            System.out.println(epic);

            for (Task task : manager.getAllSubtasksByEpicID(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}