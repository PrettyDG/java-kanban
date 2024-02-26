package Controllers;

import Models.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

    private final ArrayList<Task> viewHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (viewHistory.size()<10){
            viewHistory.add(task);
        } else {
            viewHistory.removeFirst();
            viewHistory.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return viewHistory;
    }
}