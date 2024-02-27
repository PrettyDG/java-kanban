import Controllers.InMemoryHistoryManager;
import Controllers.InMemoryTaskManager;
import Controllers.Managers;
import Controllers.TaskManager;
import Models.DefaultTask;
import Models.Epic;
import Models.Subtask;
import Models.Task;
import Utils.TaskStage;

public class Main {
    public static void main(String[] args) {
        // InMemoryTaskManager InMemoryTaskManager = new InMemoryTaskManager();

        TaskManager InMemoryTaskManager = Managers.getDefault();

        DefaultTask defaultTask1 = new DefaultTask("Уборка", "Уборка в комнате");
        defaultTask1.setStage(TaskStage.NEW);
        InMemoryTaskManager.createDefaultTask(defaultTask1);

        DefaultTask defaultTask2 = new DefaultTask("Готовка", "Приготовить ужин");
        defaultTask2.setStage(TaskStage.NEW);
        InMemoryTaskManager.createDefaultTask(defaultTask2);


        Epic epic1 = new Epic("Переезд", "Переезд в другой город");
        InMemoryTaskManager.createEpicTask(epic1);
        Subtask subtask1 = new Subtask("Сбор вещей", "Собрать вещи по коробкам");
        subtask1.setStage(TaskStage.NEW);
        InMemoryTaskManager.createSubtask(subtask1, epic1);
        Subtask subtask2 = new Subtask("Поиск билетов", "Выбрать билеты в другой город");
        subtask2.setStage(TaskStage.IN_PROGRESS);
        InMemoryTaskManager.createSubtask(subtask2, epic1);


        Epic epic2 = new Epic("Завести домаш.животное", "Выбор и покупка домашнего животного");
        InMemoryTaskManager.createEpicTask(epic2);
        Subtask subtask3 = new Subtask("Выбор породы", "Изучение особенностей пород");
        subtask3.setStage(TaskStage.IN_PROGRESS);
        InMemoryTaskManager.createSubtask(subtask3, epic2);

        subtask3.setStage(TaskStage.DONE);
        InMemoryTaskManager.updateSubtask(6, subtask3, epic2);

        InMemoryTaskManager.deleteDefaultTaskByID(1);
        InMemoryTaskManager.deleteEpicTask(2);

        InMemoryTaskManager.getEpicTaskByID(5);
        for (int i = 0; i < 8; i++) {
            InMemoryTaskManager.getDefaultTaskByID(0);
        }
        InMemoryTaskManager.getEpicTaskByID(5);
        InMemoryTaskManager.getEpicTaskByID(0);
        printAllTasks(InMemoryTaskManager);
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