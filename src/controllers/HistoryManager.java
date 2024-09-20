package controllers;

import models.Task;

import java.util.ArrayList;

public interface HistoryManager {

    void add(Task task);

    void remove(Node node);

    ArrayList<Task> getHistory();
}
