package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private final Map<Integer, Node<Task>> linkedHashMapForTask = new HashMap<>();

    private void linkLast(Task task) {
        Node<Task> newNode = new Node<>(task);
        Node<Task> oldTail = tail;

        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
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

//    private void removeNode(Node<Task> taskNode) {                                      //рабочий, но неверный вариант
//        Node<Task> currentNode = head;
//        Node<Task> previousNode = null;
//
//        while (currentNode != null) {
//
//            if (currentNode.data == taskNode.data) {
//                if (currentNode == head) {
//                    head = currentNode.next;
//                } else {
//                    previousNode.next = currentNode.next;
//                }
//            }
//
//            previousNode = currentNode;
//
//            if (currentNode.next != null) {
//                currentNode = currentNode.next;
//            } else {
//                currentNode = null;
//            }
//        }
//    }

    private void removeNode(Node<Task> taskNode) {
        if (taskNode == null) {
            return;
        }

        Node<Task> prev = taskNode.prev;    // prev и next всегда null, тк taskNode в конструкторе выставляет null полям
        Node<Task> next = taskNode.next;    // нет понимания как быть с этим, нужно создать новый конструктор для Node?

        if (prev != null) {
            prev.next = next;
            next.prev = prev;
        } else {
            head = next;
        }
    }

        @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        Node<Task> taskNode = new Node<>(task);
        if (linkedHashMapForTask.containsKey(task.getId())) {
            removeNode(taskNode);
            linkLast(task);
        } else {
            linkLast(task);
        }
        linkedHashMapForTask.put(task.getId(), taskNode);
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

