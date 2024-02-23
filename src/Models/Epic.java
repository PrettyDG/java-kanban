package Models;

import java.util.ArrayList;

public class Epic extends DefaultTask {
    private ArrayList<Integer> SubtasksID = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description);
    }

    public void setSubtasksID(int id) {
        SubtasksID.add(id);
    }

    public ArrayList<Integer> getAllSubtasksID() {
        return SubtasksID;
    }

    public void deleteSubtaskID(int id) {
        for (int i = 0; i < SubtasksID.size(); i++){
            if (SubtasksID.get(i)==id){
                SubtasksID.remove(i);
                return;
            }
        }
    }

    public void clearSubtasks() {
        SubtasksID.clear();
    }
}
