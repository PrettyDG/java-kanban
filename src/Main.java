import Controllers.TaskManager;
import Models.DefaultTask;
import Models.Epic;
import Models.Subtask;
import Utils.TaskStage;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        DefaultTask defaultTask1 = new DefaultTask("Уборка", "Уборка в комнате");
        defaultTask1.setStage(TaskStage.NEW);
        taskManager.createDefaultTask(defaultTask1);

        DefaultTask defaultTask2 = new DefaultTask("Готовка", "Приготовить ужин");
        defaultTask2.setStage(TaskStage.NEW);
        taskManager.createDefaultTask(defaultTask2);


        Epic epic1 = new Epic("Переезд", "Переезд в другой город");
        taskManager.createEpicTask(epic1);
        Subtask subtask1 = new Subtask("Сбор вещей", "Собрать вещи по коробкам");
        subtask1.setStage(TaskStage.NEW);
        taskManager.createSubtask(subtask1, epic1);
        Subtask subtask2 = new Subtask("Поиск билетов", "Выбрать билеты в другой город");
        subtask2.setStage(TaskStage.IN_PROGRESS);
        taskManager.createSubtask(subtask2, epic1);


        Epic epic2 = new Epic("Завести домаш.животное", "Выбор и покупка домашнего животного");
        taskManager.createEpicTask(epic2);
        Subtask subtask3 = new Subtask("Выбор породы", "Изучение особенностей пород");
        subtask3.setStage(TaskStage.IN_PROGRESS);
        taskManager.createSubtask(subtask3, epic2);

        subtask3.setStage(TaskStage.DONE);
        taskManager.updateSubtask(6, subtask3, epic2);

        taskManager.deleteDefaultTaskByID(1);
        taskManager.deleteEpicTask(2);
    }
}