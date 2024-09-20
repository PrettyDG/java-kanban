package Controllers;

import Models.Task;

public class Node {
    Task task;
    Node next;
    Node prev;

    public Node(Task task) {
        this.task = task;
        this.next = null;
        this.prev = null;
    }
}
