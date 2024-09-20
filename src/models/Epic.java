package models;

import java.util.ArrayList;

public class Epic extends DefaultTask implements Task {
    private ArrayList<Integer> subtasksID = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description);
    }

    public void setSubtasksID(int id) {
        subtasksID.add(id);
    }

    public ArrayList<Integer> getAllSubtasksID() {
        return subtasksID;
    }

    public void deleteSubtaskID(int id) {
        for (int i = 0; i < subtasksID.size(); i++) {
            if (subtasksID.get(i) == id) {
                subtasksID.remove(i);
                return;
            }
        }
    }

    public void clearSubtasks() {
        subtasksID.clear();
    }
}
