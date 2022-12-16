package ru.yandex.practicum.manager;

import com.sun.source.util.TaskListener;
import ru.yandex.practicum.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    Map<Integer, Node> nodeMap = new HashMap<>();
    Node firstNode;
    Node lastNode;

    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            removeNode(nodeMap.get(task.getId()));
            linkLast(task);
        } else {
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id))
            removeNode(nodeMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public void linkLast(Task task) {
        Node node = new Node(task);
        if (lastNode != null && firstNode != null) {
            node.prevNode = lastNode;
            lastNode.nextNode = node;
            lastNode = node;
            nodeMap.put(task.getId(), node);
        } else {
            firstNode = node;
            lastNode = node;
            nodeMap.put(task.getId(), node);
        }
    }

    public List<Task> getTasks() {
        List<Task> allTasksHistory = new ArrayList<>();
        if (firstNode == null && lastNode == null) {
            return allTasksHistory;
        }

        Node curNode = firstNode;
        allTasksHistory.add(curNode.anyTask);

        while (curNode.nextNode != null) {
            curNode = curNode.nextNode;
            allTasksHistory.add(curNode.anyTask);
        }

        return allTasksHistory;
    }

    public void removeNode(Node node) {
        if (node.nextNode == null && node.prevNode == null) {
            lastNode = null;
            firstNode = null;
        } else if (node.nextNode == null) {
            lastNode = node.prevNode;
            lastNode.nextNode = null;
        } else if (node.prevNode == null) {
            firstNode = node.nextNode;
            firstNode.prevNode = null;
        } else {
            node.prevNode.nextNode = node.nextNode;
            node.nextNode.prevNode = node.prevNode;
        }
        nodeMap.remove(node.anyTask.getId());
    }

    class Node {
        Node prevNode;
        Task anyTask;
        Node nextNode;

        Node (Task task) {
            anyTask = task;
        }
    }
}
