package controllers;

import exceptions.ManagerSaveException;
import models.*;
import utils.TaskStage;

import java.io.*;
import java.util.HashMap;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (FileReader fileReader = new FileReader(file);
             BufferedReader br = new BufferedReader(fileReader)) {
            br.readLine();
            HashMap<Integer, Task> epics = new HashMap<>();

            while (br.ready()) {
                String str = br.readLine();
                if (str.trim().equals("")) return manager;

                if (manager.fromStringToTask(str).getType().equals("TASK")) {
                    manager.createDefaultTask((DefaultTask) manager.fromStringToTask(str));
                } else if (manager.fromStringToTask(str).getType().equals("EPIC")) {
                    manager.createEpicTask((Epic) manager.fromStringToTask(str));
                } else {
                    manager.createSubtask((Subtask) manager.fromStringToTask(str),
                            manager.getEpicTaskByID(manager.getEpicIdFromString(str)));
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка в загрузке файла");
        }
        return manager;
    }

    @Override
    public DefaultTask createDefaultTask(DefaultTask currentTask) {
        super.createDefaultTask(currentTask);
        save();
        return currentTask;
    }

    @Override
    public DefaultTask deleteDefaultTaskByID(int id) {
        DefaultTask task = super.deleteDefaultTaskByID(id);
        save();
        return task;
    }

    @Override
    public void deleteAllDefaultTasks() {
        super.deleteAllDefaultTasks();
        save();
    }

    @Override
    public Epic createEpicTask(Epic currentTask) {
        super.createEpicTask(currentTask);
        save();
        return currentTask;
    }

    @Override
    public Epic deleteEpicTask(int id) {
        Epic epic = super.deleteEpicTask(id);
        save();
        return epic;
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Subtask createSubtask(Subtask currentSubtask, Epic currentEpic) {
        super.createSubtask(currentSubtask, currentEpic);
        save();
        return currentSubtask;
    }

    @Override
    public Subtask deleteSubtaskByID(int id) {
        Subtask task = super.deleteSubtaskByID(id);
        save();
        return task;
    }

    @Override
    public void deleteAllSubtasksForEpic(Epic currentEpic) {
        super.deleteAllSubtasksForEpic(currentEpic);
        save();
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(file, false)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                try {
                    fileWriter.write(task.toString());
                    fileWriter.write("\n");
                } catch (IOException exception) {
                    throw new ManagerSaveException("Произошла ошибка сохранения файла!");
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Произошла ошибка в сохранении!");
        }
    }

    public Integer getEpicIdFromString(String value) {
        String[] values = value.trim().split(",");
        int epicID = Integer.parseInt(values[5]);
        return epicID;
    }

    public Task fromStringToTask(String value) {
        String[] values = value.trim().split(",");

        int id = Integer.parseInt(values[0]);
        String type = values[1];
        String name = values[2];
        TaskStage status = TaskStage.valueOf(values[3]);
        String description = values[4];
        Integer epicID = values.length > 5 ? Integer.parseInt(values[5]) : null;

        try {
            switch (type) {
                case ("TASK"):
                    DefaultTask defaultTask = new DefaultTask(id, name, description, status);
                    return defaultTask;

                case ("EPIC"):
                    Epic epic = new Epic(id, name, description, status);
                    return epic;

                case ("SUBTASK"):
                    Subtask subtask = new Subtask(id, name, description, status, epicID);
                    return subtask;
            }
        } catch (NullPointerException exception) {
            throw new ManagerSaveException("epicID = null");
        }
        return null;
    }
}
