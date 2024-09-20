package Controllers;

import Models.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {


    private final HashMap<Integer, Node> tasksAndId = new HashMap<>();
    private Node head;
    private Node tail;


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        for (Integer key : tasksAndId.keySet()) {
            if (task.getId() == key) {
                remove(tasksAndId.get(key));
                tasksAndId.remove(key);
                break;
            }
        }

        Node newNode = new Node(task);
        tasksAndId.put(task.getId(), newNode);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    @Override
    public void remove(Node nodeToRemove) {
        Node current = head;

        while (current != null) {
            if (current == nodeToRemove) {
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
                return;
            }
            current = current.next;
        }
    }


    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node current = head;


        if (head != null) {
            while (head.prev != null) {
                current = head.prev;
                head.prev = head.prev.prev;
            }
            while (current != null) {
                tasks.add(current.task);
                current = current.next;
            }
        }
        return tasks;
    }
}
