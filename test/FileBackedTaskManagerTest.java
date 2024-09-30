import controllers.*;
import exceptions.ManagerSaveException;
import models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;


public class FileBackedTaskManagerTest {

    static DefaultTask defaultTask1;
    static Epic epic1;
    static Subtask subtask1;
    static File testFile = new File("src/resources/test_file.txt");
    static FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(testFile);

    @BeforeAll
    public static void beforeAll() throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))){
            writer.write("id,type,name,status,description,epic\n");
            writer.write("0,TASK,Task1,NEW,Description task1\n");
            writer.write("1,EPIC,Epic2,DONE,Description epic2\n");
            writer.write("2,SUBTASK,Sub Task2,DONE,Description sub task3,1");
        }
    }


    @Test
    public void loadAndDeleteFromFileTest() throws ManagerSaveException {
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(testFile);
        Assertions.assertNotNull(fileBackedTaskManager.getAllTasks());

        fileBackedTaskManager.deleteAllDefaultTasks();
        fileBackedTaskManager.deleteAllSubtasksForEpic(fileBackedTaskManager.getEpicTaskByID(1));
        fileBackedTaskManager.deleteAllEpics();
    }
}
