public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        DefaultTask defaultTask1 = new DefaultTask("Уборка", "Уборка в комнате", TaskStage.NEW);
        taskManager.createDefaultTask(defaultTask1);

        DefaultTask defaultTask2 = new DefaultTask("Готовка", "Приготовить ужин"
                , TaskStage.NEW);
        taskManager.createDefaultTask(defaultTask2);


        Epic epic1 = new Epic("Переезд", "Переезд в другой город", TaskStage.NEW);
        taskManager.createEpicTask(epic1);
        Subtask subtask1 = new Subtask("Сбор вещей", "Собрать вещи по коробкам"
                , TaskStage.IN_PROGRESS, epic1);
        taskManager.createSubtask(subtask1, epic1);
        Subtask subtask2 = new Subtask("Поиск билетов", "Выбрать билеты в другой город"
                , TaskStage.IN_PROGRESS, epic1);
        taskManager.createSubtask(subtask2,epic1);


        Epic epic2 = new Epic("Завести домаш.животное", "Выбор и покупка домашнего животного"
                , TaskStage.NEW);
        taskManager.createEpicTask(epic2);
        Subtask subtask3 = new Subtask("Выбор породы", "Изучение особенностей пород"
                , TaskStage.IN_PROGRESS, epic2);
        taskManager.createSubtask(subtask3, epic2);

        taskManager.printDefaultTasks();
        taskManager.printEpicTasks();
        taskManager.printSubtask();

        taskManager.deleteDefaultTask(0);
        taskManager.printDefaultTasks();

        taskManager.deleteEpicTask(2);
        taskManager.printEpicTasks();
        taskManager.printSubtask();
    }
}