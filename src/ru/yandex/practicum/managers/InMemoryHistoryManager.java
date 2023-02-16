package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    CustomLinkedList linkedList = new CustomLinkedList();
    Map<Integer, CustomLinkedList.Node> nodeMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (nodeMap.containsKey(task.getId())) {
               linkedList.removeNode(nodeMap.get(task.getId()));
            }
            linkedList.linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        linkedList.removeNode(nodeMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return linkedList.getTasks();
    }

    class CustomLinkedList {
        Node firstNode;
        Node lastNode;

        public void linkLast(Task task) {
            Node node = new Node(task);
            if (lastNode != null && firstNode != null) {
                node.prevNode = lastNode;
                lastNode.nextNode = node;
            } else {
                firstNode = node;
            }
            lastNode = node;
            nodeMap.put(task.getId(), node);
        }

        public List<Task> getTasks() {
            List<Task> allTasksHistory = new ArrayList<>();

            Node curNode = firstNode;

            while (curNode != null) {
                allTasksHistory.add(curNode.anyTask);
                curNode = curNode.nextNode;
            }

            return allTasksHistory;
        }

        public void removeNode(Node node) {
            if (node != null) {
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
}
