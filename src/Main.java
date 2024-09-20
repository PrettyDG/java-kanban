import controllers.Managers;
import controllers.TaskManager;
import models.DefaultTask;
import models.Epic;
import models.Subtask;
import models.Task;
import utils.TaskStage;

public class Main {
    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Managers.getDefault();

        DefaultTask defaultTask1 = new DefaultTask("Уборка", "Уборка в комнате");
        defaultTask1.setStage(TaskStage.NEW);
        inMemoryTaskManager.createDefaultTask(defaultTask1);

        DefaultTask defaultTask2 = new DefaultTask("Готовка", "Приготовить ужин");
        defaultTask2.setStage(TaskStage.NEW);
        inMemoryTaskManager.createDefaultTask(defaultTask2);


        Epic epic1 = new Epic("Переезд", "Переезд в другой город");
        inMemoryTaskManager.createEpicTask(epic1);
        Subtask subtask1 = new Subtask("Сбор вещей", "Собрать вещи по коробкам");
        subtask1.setStage(TaskStage.NEW);
        inMemoryTaskManager.createSubtask(subtask1, epic1);
        Subtask subtask2 = new Subtask("Поиск билетов", "Выбрать билеты в другой город");
        subtask2.setStage(TaskStage.IN_PROGRESS);
        inMemoryTaskManager.createSubtask(subtask2, epic1);
        Subtask subtask3 = new Subtask("Выбор жилья", "Необходимо выбрать новое жильё");
        subtask3.setStage(TaskStage.IN_PROGRESS);
        inMemoryTaskManager.createSubtask(subtask3, epic1);
        subtask3.setStage(TaskStage.DONE);
        inMemoryTaskManager.updateSubtask(5, subtask3, epic1);


        Epic epic2 = new Epic("Завести домаш.животное", "Выбор и покупка домашнего животного");
        inMemoryTaskManager.createEpicTask(epic2);

        inMemoryTaskManager.getEpicTaskByID(3);
        inMemoryTaskManager.getEpicTaskByID(6);
        inMemoryTaskManager.getEpicTaskByID(3);

        printAllTasks(inMemoryTaskManager);

        inMemoryTaskManager.deleteDefaultTaskByID(1);

        printAllTasks(inMemoryTaskManager);

        inMemoryTaskManager.deleteEpicTask(2);

        printAllTasks(inMemoryTaskManager);
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