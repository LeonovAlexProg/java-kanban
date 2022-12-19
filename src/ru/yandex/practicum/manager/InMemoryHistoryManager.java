package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    Map<Integer, Node> nodeMap = new HashMap<>();
    Node firstNode;
    Node lastNode;

    @Override
    public void add(Task task) {
        if (task != null) {
            if (nodeMap.containsKey(task.getId())) {
                removeNode(nodeMap.get(task.getId()));
            }
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
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
        } else {
            firstNode = node;
        }
        lastNode = node;
        nodeMap.put(task.getId(), node);
    }

    public List<Task> getTasks() {
        List<Task> allTasksHistory = new ArrayList<>();

        Node curNode = firstNode;

        if (curNode != null) {
            /*В ревью было предложено поменять местами вызовы методов, однако если их поменять, то последняя нода в
            истории просмотров не проходит в цикл. Я улучшил работу метода добавив проверку на null,
            а так же использовал цикл for вместо while
             */
//            while (curNode.nextNode != null) {
//                curNode = curNode.nextNode;
//                allTasksHistory.add(curNode.anyTask);
//            }

            for (int i = 0; i < nodeMap.size(); i++) {
                allTasksHistory.add(curNode.anyTask);
                curNode = curNode.nextNode;
            }
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
