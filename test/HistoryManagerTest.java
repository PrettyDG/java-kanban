import controllers.FileBackedTaskManager;
import models.DefaultTask;
import models.Epic;
import models.Subtask;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HistoryManagerTest {

    static DefaultTask defaultTask1;
    static Epic epic1;
    static Subtask subtask1;
    static File testFile = new File("src/resources/test_file.txt");
    static FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(testFile);

    @BeforeAll
    public static void beforeAll() throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("id,type,name,status,description,startTime,duration,epicID\n");
            writer.write("0,TASK,Task1,NEW,Description task1,16:00 04.10.2024,30\n");
            writer.write("1,EPIC,Epic2,DONE,Description epic2,17:00 04.10.2024,30\n");
            writer.write("2,SUBTASK,Sub Task2,DONE,Description subtask2,18:00 04.10.2024,30,1\n");
            writer.write("3,SUBTASK,Sub Task3,DONE,Description subtask3,19:00 04.10.2024,30,1\n");
            writer.write("4,SUBTASK,Sub Task4,DONE,Description subtask4,19:00 04.10.2024,30,1\n");
        }
    }

    @Test
    public void checkHistory() {
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(testFile);
        System.out.println(fileBackedTaskManager.getHistory()); // История должна быть пуста

        fileBackedTaskManager.getTaskforUserByID(0);
        System.out.println(fileBackedTaskManager.getHistory()); // История должна содержать 1 задачу

        fileBackedTaskManager.getTaskforUserByID(1);
        fileBackedTaskManager.getTaskforUserByID(2);
        fileBackedTaskManager.getTaskforUserByID(0);
        System.out.println(fileBackedTaskManager.getHistory()); // История должна содержать 3 задачи, последняя Default

        fileBackedTaskManager.deleteAllDefaultTasks();
        System.out.println(fileBackedTaskManager.getHistory()); // История должна содержать 2 задачи и удалить Default

        fileBackedTaskManager.clearEverything();
    }
}
