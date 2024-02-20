import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    HashMap<Integer, DefaultTask> defaultTasksHash = new HashMap<>();
    HashMap<Integer, Epic> epicTasksHash = new HashMap<>();
    HashMap<Integer, Subtask> subtaskEpicHash = new HashMap<>();
    static int IDnumber = 0;

    DefaultTask createDefaultTask(DefaultTask currentTask) {
        defaultTasksHash.put(IDnumber, currentTask);
        IDnumber++;
        return currentTask;
    }

    DefaultTask updateDefaultTask(int id) {
        DefaultTask currentTask = defaultTasksHash.get(id);
        defaultTasksHash.put(IDnumber, currentTask);
        return currentTask;
    }

    DefaultTask deleteDefaultTask(int id) {
        DefaultTask currentTask = defaultTasksHash.get(id);
        defaultTasksHash.remove(id);
        return currentTask;
    }

    Epic createEpicTask(Epic currentTask){
        epicTasksHash.put(IDnumber, currentTask);
        IDnumber++;
        return currentTask;
    }

    Epic updateEpicTask(int id){
        Epic currentTask = epicTasksHash.get(id);
        epicTasksHash.put(id, currentTask);
        return currentTask;
    }

    Epic deleteEpicTask(int id){
        Epic currentTask = epicTasksHash.get(id);
        epicTasksHash.remove(id);
        return currentTask;
    }
    void printDefaultTasks(){
        System.out.println(defaultTasksHash);
    }
}
