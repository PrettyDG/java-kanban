import controllers.*;
import exceptions.ManagerSaveException;
import models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.TaskStage;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class FileBackedTaskManagerTest {

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
    public void loadAndDeleteFromFile() throws ManagerSaveException {
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(testFile);
        Assertions.assertNotNull(fileBackedTaskManager.getAllTasks());

        System.out.println("Время завершения эпика ID1 - " + fileBackedTaskManager.getEpicTaskByID(1).getEndTime(fileBackedTaskManager));

        System.out.println(fileBackedTaskManager.getPrioritizedTasks());
        fileBackedTaskManager.deleteAllDefaultTasks();
        fileBackedTaskManager.deleteAllSubtasksForEpic(fileBackedTaskManager.getEpicTaskByID(1));
        fileBackedTaskManager.deleteAllEpics();
        fileBackedTaskManager.clearEverything();
    }

    @Test
    public void checkForTaskCrossing() {
        DefaultTask defaultTask = new DefaultTask(0, "checkForCrossing", "crossing description"
                , TaskStage.NEW, LocalDateTime.of(2024, 10, 4, 19, 0), Duration.ofMinutes(30));
        DefaultTask defaultTask2 = new DefaultTask(1, "checkForCrossing2", "crossing description2"
                , TaskStage.NEW, LocalDateTime.of(2024, 10, 4, 19, 0), Duration.ofMinutes(30));

        fileBackedTaskManager.createDefaultTask(defaultTask);
        fileBackedTaskManager.createDefaultTask(defaultTask2);
        Assertions.assertNull(fileBackedTaskManager.getDefaultTaskByID(1));

        fileBackedTaskManager.deleteAllDefaultTasks();
        fileBackedTaskManager.clearEverything();
    }

    @Test
    public void checkForEpicStatus() {
        Epic epic1 = new Epic(0, "Epic1", "Epic1 description1", TaskStage.NEW);
        Subtask subtask1 = new Subtask(1, "Subtask1", "Subtask1 description2"
                , TaskStage.NEW, 0, LocalDateTime.now(), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2, "Subtask2", "Subtask2 description3"
                , TaskStage.NEW, 0, LocalDateTime.now().minusHours(5), Duration.ofMinutes(30));

        fileBackedTaskManager.createEpicTask(epic1);
        fileBackedTaskManager.createSubtask(subtask1, epic1);
        fileBackedTaskManager.createSubtask(subtask2, epic1);

        fileBackedTaskManager.getAllSubtasksByEpicID(0).stream().forEach(SUBTASK -> SUBTASK.setStage(TaskStage.NEW));
        System.out.println(epic1); // Подзадачи со статусами NEW

        subtask1.setStage(TaskStage.DONE);
        fileBackedTaskManager.updateSubtask(1, subtask1, epic1);
        System.out.println(epic1);  // Подзадачи со статусами NEW и DONE

        subtask2.setStage(TaskStage.DONE);
        fileBackedTaskManager.updateSubtask(2, subtask2, epic1);
        System.out.println(epic1); // Подзадачи со статусами DONE

        subtask1.setStage(TaskStage.IN_PROGRESS);
        subtask2.setStage(TaskStage.IN_PROGRESS);
        fileBackedTaskManager.updateSubtask(1, subtask1, epic1);
        fileBackedTaskManager.updateSubtask(2, subtask2, epic1);
        System.out.println(epic1); // Подзадачи со статусами IN_PROGRESS

        fileBackedTaskManager.clearEverything();
    }

    @Test
    public void testLoadFromNullFile() {
        File zeroFile = new File("zero_file.csv");

        Assertions.assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(zeroFile);
        }, "Ожидалось исключение ManagerSaveException");
    }
}
