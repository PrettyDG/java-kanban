package controllers;

import models.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    public static class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Task task) {
            this.task = task;
            this.next = null;
            this.prev = null;
        }
    }

    private final HashMap<Integer, Node> tasksAndId = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (tasksAndId.containsKey(task.getId())) {
            remove(task.getId());
            tasksAndId.remove(task.getId());
        }

        Node newNode = new Node(task);
        tasksAndId.put(task.getId(), newNode);

        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;
    }

    @Override
    public void remove(int id) {
        Node current = tasksAndId.get(id);
        tasksAndId.remove(id);
        if (current == null) {
            return;
        }

        if (current == head) {
            head = current.next;
            if (head != null) {
                head.prev = null;
            }
        } else {
            if (current.next != null) {
                current.next.prev = current.prev;
            }
            if (current.prev != null) {
                current.prev.next = current.next;
            }
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node current = head;

        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }
}