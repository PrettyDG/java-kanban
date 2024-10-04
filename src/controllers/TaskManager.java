package controllers;

import exceptions.ManagerSaveException;
import models.DefaultTask;
import models.Epic;
import models.Subtask;
import models.Task;

import java.util.ArrayList;

public interface TaskManager {
    DefaultTask createDefaultTask(DefaultTask currentTask) throws ManagerSaveException;

    DefaultTask updateDefaultTask(int id);

    DefaultTask deleteDefaultTaskByID(int id) throws ManagerSaveException;

    void deleteAllDefaultTasks() throws ManagerSaveException;

    ArrayList<DefaultTask> getAllDefaultTasks();

    DefaultTask getDefaultTaskByID(int id);

    Epic createEpicTask(Epic currentTask) throws ManagerSaveException;

    Epic deleteEpicTask(int id) throws ManagerSaveException;

    void deleteAllEpics() throws ManagerSaveException;

    ArrayList<Epic> getEpicTasks();

    Epic getEpicTaskByID(int id);

    ArrayList<Subtask> getAllSubtasksByEpicID(int id);

    Subtask createSubtask(Subtask currentSubtask, Epic currentEpic) throws ManagerSaveException;

    Subtask updateSubtask(int id, Subtask newSubTask, Epic currentEpic);

    Subtask deleteSubtaskByID(int id) throws ManagerSaveException;

    void deleteAllSubtasksForEpic(Epic currentEpic) throws ManagerSaveException;

    ArrayList<Subtask> getAllSubtasks();

    Subtask getSubtaskByID(int id);

    ArrayList<Task> getHistory();

    Task getTaskforUserByID(int id);
}
