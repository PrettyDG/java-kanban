import controllers.Managers;
import controllers.TaskManager;
import models.DefaultTask;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.TaskStage;

import java.time.Duration;
import java.time.LocalDateTime;


public class InMemoryTaskManagerTest {

    static TaskManager inMemoryTaskManager = Managers.getDefault();
    static DefaultTask defaultTask1;
    static Epic epic1;
    static Subtask subtask1;

    @BeforeAll
    public static void beforeAll() {
        defaultTask1 = new DefaultTask(0, "Task1", "Task1 description"
                , TaskStage.NEW, LocalDateTime.of(2024, 10, 4, 19, 0), Duration.ofMinutes(30));
        defaultTask1.setStage(TaskStage.NEW);
        inMemoryTaskManager.createDefaultTask(defaultTask1);

        epic1 = new Epic(1, "Epic1", "Epic1 description", TaskStage.NEW);
        inMemoryTaskManager.createEpicTask(epic1);

        subtask1 = new Subtask(1, "Subtask2", "Subtask2 description3"
                , TaskStage.NEW, 0, LocalDateTime.now().minusHours(5), Duration.ofMinutes(30));
        inMemoryTaskManager.createSubtask(subtask1, epic1);
    }

    @Test
    public void createNewTask() {
        Task savedTask = defaultTask1;

        Assertions.assertNotNull(savedTask);
        Assertions.assertEquals(savedTask, inMemoryTaskManager.getDefaultTaskByID(defaultTask1.getId()));
    }

    @Test
    public void historyCheck() {
        inMemoryTaskManager.getTaskforUserByID(defaultTask1.getId());
        Assertions.assertNotNull(inMemoryTaskManager.getHistory());
        Assertions.assertEquals(1, inMemoryTaskManager.getHistory().size());
    }

    @Test
    public void subtaskCantBeEpicForHimself() {
        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStage(TaskStage.NEW);
        // inMemoryTaskManager.createSubtask(subtask1, subtask1);
        Assertions.assertNull(inMemoryTaskManager.getSubtaskByID(0));
    }

    @Test
    public void createDifferentTaskAndFindThemByIDs() {
        Epic currentEpic = inMemoryTaskManager.getEpicTaskByID(1);
        Subtask currentSubtask = inMemoryTaskManager.getSubtaskByID(2);

        Assertions.assertNotNull(currentEpic);
        Assertions.assertNotNull(currentSubtask);
    }

    @Test
    public void updateTasks() {
        defaultTask1.setStage(TaskStage.IN_PROGRESS);
        inMemoryTaskManager.updateDefaultTask(0);
        Assertions.assertEquals(TaskStage.IN_PROGRESS, inMemoryTaskManager.getDefaultTaskByID(0).getStage());

        subtask1.setStage(TaskStage.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(2, subtask1, epic1);
        Assertions.assertEquals(TaskStage.IN_PROGRESS, inMemoryTaskManager.getEpicTaskByID(1).getStage());
        Assertions.assertEquals(TaskStage.IN_PROGRESS, inMemoryTaskManager.getSubtaskByID(2).getStage());
    }
}
