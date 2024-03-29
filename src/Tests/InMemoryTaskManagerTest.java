package Tests;

import Controllers.*;
import Models.DefaultTask;
import Models.Epic;
import Models.Subtask;
import Models.Task;
import Utils.TaskStage;
import com.sun.tools.javac.Main;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AccessFlag;


public class InMemoryTaskManagerTest {

    static TaskManager inMemoryTaskManager = Managers.getDefault();
    static DefaultTask defaultTask1;
    static InMemoryHistoryManager historyManager;
    static Epic epic1;
    static Subtask subtask1;

    @BeforeAll
    public static void beforeAll(){
        defaultTask1 = new DefaultTask("Task1", "Task1 description");
        defaultTask1.setStage(TaskStage.NEW);
        inMemoryTaskManager.createDefaultTask(defaultTask1);

        epic1 = new Epic("Epic1", "Epic1 description");
        inMemoryTaskManager.createEpicTask(epic1);

        subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStage(TaskStage.NEW);
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
        inMemoryTaskManager.getDefaultTaskByID(defaultTask1.getId());
        Assertions.assertNotNull(inMemoryTaskManager.getHistory());
        Assertions.assertEquals(1, inMemoryTaskManager.getHistory().size());
    }

    @Test
    public void subtaskCantBeEpicForHimself(){
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
