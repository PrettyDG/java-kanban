import controllers.FileBackedTaskManager;
import controllers.TaskManager;
import models.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        File file = new File("src/resources/file.txt");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,startTime,duration,epicID\n");
            writer.write("0,TASK,Task1,NEW,Description task1,16:00 04.10.2024,30\n");
            writer.write("1,EPIC,Epic2,DONE,Description epic2,17:00 04.10.2024,30\n");
            writer.write("2,SUBTASK,Sub Task2,DONE,Description subtask2,18:00 04.10.2024,30,1\n");
            writer.write("3,SUBTASK,Sub Task3,NEW,Description subtask3,19:00 04.10.2024,30,1\n");
            writer.write("4,SUBTASK,Sub Task4,DONE,Description subtask4,19:00 04.10.2024,30,1\n");
        } catch (IOException exception) {
            System.out.println("Ошибка записи в файл");
        }

        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);

        System.out.println("Время завершения эпика ID1 - " + fileBackedTaskManager.getEpicTaskByID(1).getEndTime(fileBackedTaskManager));


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