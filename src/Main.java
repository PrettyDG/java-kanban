import controllers.FileBackedTaskManager;
import controllers.ManagerSaveException;
import controllers.Managers;
import controllers.TaskManager;
import models.DefaultTask;
import models.Epic;
import models.Subtask;
import models.Task;
import utils.TaskStage;

import java.io.*;

public class Main {
    public static void main(String[] args) throws ManagerSaveException {

        File file = new File("file.txt");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        //       fileBackedTaskManager.loadFromFile(file);

        DefaultTask defaultTask1 = new DefaultTask("Уборка", "Уборка в комнате");
        defaultTask1.setStage(TaskStage.NEW);
        fileBackedTaskManager.createDefaultTask(defaultTask1);

        DefaultTask defaultTask2 = new DefaultTask("Готовка", "Приготовить ужин");
        defaultTask2.setStage(TaskStage.NEW);
        fileBackedTaskManager.createDefaultTask(defaultTask2);


        Epic epic1 = new Epic("Переезд", "Переезд в другой город");
        fileBackedTaskManager.createEpicTask(epic1);
        Subtask subtask1 = new Subtask("Сбор вещей", "Собрать вещи по коробкам");
        subtask1.setStage(TaskStage.NEW);
        fileBackedTaskManager.createSubtask(subtask1, epic1);
        Subtask subtask2 = new Subtask("Поиск билетов", "Выбрать билеты в другой город");
        subtask2.setStage(TaskStage.IN_PROGRESS);
        fileBackedTaskManager.createSubtask(subtask2, epic1);
        Subtask subtask3 = new Subtask("Выбор жилья", "Необходимо выбрать новое жильё");
        subtask3.setStage(TaskStage.IN_PROGRESS);
        fileBackedTaskManager.createSubtask(subtask3, epic1);
        subtask3.setStage(TaskStage.DONE);
        fileBackedTaskManager.updateSubtask(5, subtask3, epic1);


        Epic epic2 = new Epic("Завести домаш.животное", "Выбор и покупка домашнего животного");
        fileBackedTaskManager.createEpicTask(epic2);

        fileBackedTaskManager.getDefaultTaskByID(1);
        fileBackedTaskManager.getEpicTaskByID(2);
        fileBackedTaskManager.getEpicTaskByID(6);


        printAllTasks(fileBackedTaskManager);

        fileBackedTaskManager.getDefaultTaskByID(1);
        fileBackedTaskManager.deleteDefaultTaskByID(1);

        printAllTasks(fileBackedTaskManager);

        fileBackedTaskManager.deleteEpicTask(2);
        fileBackedTaskManager.deleteAllDefaultTasks();

        DefaultTask defaultTask5 = new DefaultTask("Уборка", "Уборка в комнате");
        defaultTask5.setStage(TaskStage.NEW);
        fileBackedTaskManager.createDefaultTask(defaultTask5);

        printAllTasks(fileBackedTaskManager);
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