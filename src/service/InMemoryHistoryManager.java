package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private final Map<Integer, Node<Task>> linkedHashMapForTask = new HashMap<>();

    private void linkLast(Task task) {
        Node<Task> newNode = new Node<>(task, tail, null);

        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }

    private List<Task> getTasks() {
        Node<Task> currentNode = head;

        List<Task> tasks = new ArrayList<>();

        while (currentNode != null) {
            tasks.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    private void removeNode(Node<Task> taskNode) {
        if (taskNode == null) {
            return;
        }

        Node<Task> next = taskNode.next;
        Node<Task> prev = taskNode.prev;

        if (prev == null) {
            next.prev = null;
            head = next;
        } else if (next == null) {
            prev.next = null;
            tail = prev;
        } else {
            next.prev = taskNode.prev;
            prev.next = taskNode.next;
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (linkedHashMapForTask.containsKey(task.getId())) {
            Node<Task> taskNode = linkedHashMapForTask.remove(task.getId());
            removeNode(taskNode);
        }

        linkLast(task);
        linkedHashMapForTask.put(task.getId(), tail);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node<Task> taskNode = linkedHashMapForTask.get(id);
        removeNode(taskNode);
    }
}

