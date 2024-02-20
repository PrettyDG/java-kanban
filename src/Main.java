import java.util.Scanner;

public class Main {
    static Scanner sc;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        while (true) {
            printMenu();
            int menuNumber = sc.nextInt();
            switch (menuNumber) {
                case 1:
                    System.out.println("Выберите тип новой задачи: 1 - Обычная задача, 2 - Эпик, 3 - Подзадача");
                    int taskTypeNumber = sc.nextInt();
                    switch (taskTypeNumber) {
                        case 1:
                            DefaultTask defaultTask = new DefaultTask("Уборка", "Уборка дома", TaskStage.NEW);
                            taskManager.createDefaultTask(defaultTask);
                            break;
                        case 2:

                            break;
                        case 3:

                            break;
                        default:
                            System.out.println("Такого номера задачи нет");
                    }
                    break;
                case 2:

                    break;
                case 3:
                    taskManager.printDefaultTasks();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Такого номера не существует");
            }
        }
    }

    static void printMenu() {
        System.out.println("Введите номер меню:");
        System.out.println("1 - Добавить новую задачу");
        System.out.println("2 - Поменять статус задачи");
        System.out.println("3 - Список задач");
        System.out.println("4 - Выход");
    }
}
