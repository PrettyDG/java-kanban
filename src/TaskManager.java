import java.util.ArrayList;
import java.util.HashMap;

class TaskManager {
    HashMap<Integer, DefaultTask> defaultTasksHash = new HashMap<>();
    HashMap<Integer, Epic> epicTasksHash = new HashMap<>();
    HashMap<Integer, Subtask> subtaskHash = new HashMap<>();
    HashMap<Epic, ArrayList<Subtask>> subtasksForEpic = new HashMap<>();
    ArrayList<Subtask> subtasks;
    static int idNumberForTasks = 0;


    DefaultTask createDefaultTask(DefaultTask currentTask) {
        defaultTasksHash.put(idNumberForTasks, currentTask);
        currentTask.setId(idNumberForTasks);
        idNumberForTasks++;
        return currentTask;
    }

    DefaultTask updateDefaultTask(int id) {
        DefaultTask currentTask = defaultTasksHash.get(id);
        defaultTasksHash.put(idNumberForTasks, currentTask);
        return currentTask;
    }

    DefaultTask deleteDefaultTask(int id) {
        DefaultTask currentTask = defaultTasksHash.get(id);
        defaultTasksHash.remove(id);
        return currentTask;
    }

    void deleteAllDefaultTasks() {
        defaultTasksHash.clear();
    }

    void printDefaultTasks() {
        System.out.println(defaultTasksHash);
    }

    void printDefaultTaskByID(int id) {
        System.out.println(defaultTasksHash.get(id));
    }


    Epic createEpicTask(Epic currentTask) {
        epicTasksHash.put(idNumberForTasks, currentTask);
        currentTask.setId(idNumberForTasks);
        subtasks = new ArrayList<>();
        subtasksForEpic.put(currentTask, subtasks);
        idNumberForTasks++;
        return currentTask;
    }

    private Epic updateEpicTask(int id) {
        //тут работает что-то неправильно - не обновляет статус ЭПИКа при обновлении подзадачи, не могу разобраться в чём проблема
        Epic currentTask = epicTasksHash.get(id);
        boolean isEpicDone = true;
        boolean isEpicNew = true;

        for (Subtask currentSubtasks : subtasksForEpic.get(currentTask)) {
            if (currentSubtasks.stage != TaskStage.DONE) {
                isEpicDone = false;
                break;
            }
            if (currentSubtasks.stage != TaskStage.NEW) {
                isEpicNew = false;
                break;
            }
        }

        if (isEpicDone) {
            currentTask.stage = TaskStage.DONE;
        } else if (isEpicNew) {
            currentTask.stage = TaskStage.NEW;
        } else {
            currentTask.stage = TaskStage.IN_PROGRESS;
        }

        return currentTask;
    }

    Epic deleteEpicTask(int id) {
        Epic currentTask = epicTasksHash.get(id);
        epicTasksHash.remove(id);
        for (Subtask currentSubtasks : subtasksForEpic.get(currentTask)) {
            deleteSubtask(currentSubtasks.id);
        }
        return currentTask;
    }

    void deleteAllEpics() {
        epicTasksHash.clear();
        subtaskHash.clear();
        subtasksForEpic.clear();
    }

    void printEpicTasks() {
        System.out.println(epicTasksHash);
    }

    void printEpicTaskByID(int id) {
        System.out.println(epicTasksHash.get(id));
    }

    void printSubtasksByEpicID(int id) {
        Epic currentEpic = epicTasksHash.get(id);
        for (Subtask currentSubtask : subtasksForEpic.get(currentEpic)) {
            printSubtaskByID(currentSubtask.id);
        }
    }


    Subtask createSubtask(Subtask currentSubtask, Epic currentEpic) {
        subtaskHash.put(idNumberForTasks, currentSubtask);
        currentSubtask.setId(idNumberForTasks);
        currentSubtask.setEpicID(currentEpic.id);
        subtasks.add(currentSubtask);
        currentEpic.setSubtasksID(idNumberForTasks);
        idNumberForTasks++;
        updateEpicTask(currentEpic.id);
        return currentSubtask;
    }

    Subtask updateSubtask(int id, Subtask newSubTask) {
        Subtask beforeSubtask = subtaskHash.get(id);
        subtaskHash.put(idNumberForTasks, newSubTask);
        updateEpicTask(beforeSubtask.epicID);
        return newSubTask;
    }

    Subtask deleteSubtask(int id) {
        Subtask currentTask = subtaskHash.get(id);
        epicTasksHash.remove(id);
        subtaskHash.remove(id);
        subtasks.remove(currentTask);
        return currentTask;
    }

    void deleteAllSubtasksForEpic(int epicID) {
        Epic currentEpic = epicTasksHash.get(epicID);
        subtasksForEpic.get(currentEpic);
        subtasksForEpic.clear();
    }

    void printSubtask() {
        System.out.println(subtaskHash);
    }

    void printSubtaskByID(int id) {
        System.out.println(subtaskHash.get(id));
    }
}