package controllers;

import models.*;
import utils.TaskStage;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(new File("file.txt"));

        try (FileReader fileReader = new FileReader("file.txt");
             BufferedReader br = new BufferedReader(fileReader)) {
            br.readLine();

            while (br.ready()) {
                String str = br.readLine();

                if (manager.fromStringToTask(str).getType().equals("TASK")) {
                    super.createDefaultTask((DefaultTask) manager.fromStringToTask(str));
                } else if (manager.fromStringToTask(str).getType().equals("EPIC")) {
                    super.createEpicTask((Epic) manager.fromStringToTask(str));
                } else {
                    super.createSubtask((Subtask) manager.fromStringToTask(str),
                            getEpicTaskByID(str.indexOf(Integer.valueOf(str.substring(str.length() - 2)))));
                }
            }
        } catch (FileNotFoundException exception) {
            System.out.println("Файл не найден");
        } catch (IOException exception) {
            System.out.println("Ошибка загрузки файла");
        }
        return manager;
    }

    @Override
    public DefaultTask createDefaultTask(DefaultTask currentTask) throws ManagerSaveException {
        super.createDefaultTask(currentTask);
        save();
        return currentTask;
    }

    @Override
    public DefaultTask deleteDefaultTaskByID(int id) throws ManagerSaveException {
        DefaultTask task = super.deleteDefaultTaskByID(id);
        save();
        return task;
    }

    @Override
    public void deleteAllDefaultTasks() throws ManagerSaveException {
        super.deleteAllDefaultTasks();
        save();
    }

    @Override
    public Epic createEpicTask(Epic currentTask) throws ManagerSaveException {
        super.createEpicTask(currentTask);
        save();
        return currentTask;
    }

    @Override
    public Epic deleteEpicTask(int id) throws ManagerSaveException {
        Epic epic = super.deleteEpicTask(id);
        save();
        return epic;
    }

    @Override
    public void deleteAllEpics() throws ManagerSaveException {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Subtask createSubtask(Subtask currentSubtask, Epic currentEpic) throws ManagerSaveException {
        super.createSubtask(currentSubtask, currentEpic);
        save();
        return currentSubtask;
    }

    @Override
    public Subtask deleteSubtaskByID(int id) throws ManagerSaveException {
        Subtask task = super.deleteSubtaskByID(id);
        save();
        return task;
    }

    @Override
    public void deleteAllSubtasksForEpic(Epic currentEpic) throws ManagerSaveException {
        super.deleteAllSubtasksForEpic(currentEpic);
        save();
    }

    public void save() throws ManagerSaveException {
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

    public Task fromStringToTask(String value) throws ManagerSaveException {
        String[] values = value.split(",");
        Task task;
        if (value.contains("SUBTASK")) {
            task = new Subtask(Integer.parseInt(values[0]), values[2], values[4],
                    TaskStage.valueOf(values[3]), Integer.parseInt(values[5]));
        } else if (value.contains("EPIC")) {
            task = new Epic(Integer.parseInt(values[0]), values[2], values[4], TaskStage.valueOf(values[3]));
        } else {
            task = new DefaultTask(Integer.parseInt(values[0]), values[2], values[4], TaskStage.valueOf(values[3]));
        }
        return task;
    }
}
