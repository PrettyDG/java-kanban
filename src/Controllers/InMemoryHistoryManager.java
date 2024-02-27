package Controllers;

import Models.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

    private final ArrayList<Task> viewHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        if(task == null){
            return;
        }
        if (viewHistory.size()>9){
            viewHistory.removeFirst();
        }
        viewHistory.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<Task>(viewHistory);
    }
}
