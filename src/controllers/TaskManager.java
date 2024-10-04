package controllers;

import models.DefaultTask;
import models.Epic;
import models.Subtask;
import models.Task;

import java.util.ArrayList;

public interface TaskManager {
    DefaultTask createDefaultTask(DefaultTask currentTask);

    DefaultTask updateDefaultTask(int id);

    DefaultTask deleteDefaultTaskByID(int id);

    void deleteAllDefaultTasks();

    ArrayList<DefaultTask> getAllDefaultTasks();

    DefaultTask getDefaultTaskByID(int id);

    Epic createEpicTask(Epic currentTask);

    Epic deleteEpicTask(int id);

    void deleteAllEpics();

    ArrayList<Epic> getEpicTasks();

    Epic getEpicTaskByID(int id);

    ArrayList<Subtask> getAllSubtasksByEpicID(int id);

    Subtask createSubtask(Subtask currentSubtask, Epic currentEpic);

    Subtask updateSubtask(int id, Subtask newSubTask, Epic currentEpic);

    Subtask deleteSubtaskByID(int id);

    void deleteAllSubtasksForEpic(Epic currentEpic);

    ArrayList<Subtask> getAllSubtasks();

    Subtask getSubtaskByID(int id);

    ArrayList<Task> getHistory();

    Task getTaskforUserByID(int id);
}
