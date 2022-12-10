package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    public List<Task> history = new ArrayList<>();
    Map<Integer, Node<Task>> linkedHashMapForTask = new HashMap<>();

    public void linkLast(Task task) {
        Node<Task> newNode = new Node<>(task);
        Node<Task> currentNode = head;

        if (head == null) {
            head = newNode;
        } else {
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
    }

    public void getTasks() {
        Node<Task> currentNode = head;

        if (currentNode == null) {
            return;
        } else {
            history.add(currentNode.data);

            while (currentNode.next != null) {
                currentNode = currentNode.next;
                history.add(currentNode.data);
            }
        }
    }

    public void removeNode(Node<Task> taskNode) {
        Node<Task> currentNode = head;
        Node<Task> previousNode = null;

        while (currentNode != null) {

            if (currentNode.data == taskNode.data) {
                if (currentNode == head) {
                    head = currentNode.next;
                } else {
                    previousNode.next = currentNode.next;
                }
            }

            previousNode = currentNode;

            if (currentNode.next != null) {
                currentNode = currentNode.next;
            } else {
                currentNode = null;
            }
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        } else {
            Node<Task> taskNode = new Node<>(task);
            if (linkedHashMapForTask.containsKey(task.getId())) {
                removeNode(taskNode);
                linkLast(task);
            } else {
                linkLast(task);
            }
            linkedHashMapForTask.put(task.getId(), taskNode);
        }
    }

    @Override
    public List<Task> getHistory() {
        history.clear();
        getTasks();
        return history;
    }

    @Override
    public void remove(int id) {
        Node<Task> taskNode = linkedHashMapForTask.get(id);
        removeNode(taskNode);
    }

//        for (Task task : history) {
//            if (task.getId() == id) {
//                history.remove(id);
//            }
//        }

}

