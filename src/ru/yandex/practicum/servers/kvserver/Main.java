package ru.yandex.practicum.servers.kvserver;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.HttpTaskManager;
import ru.yandex.practicum.manager.Manager;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.servers.client.KVTaskClient;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();

        HttpTaskManager manager = (HttpTaskManager) Manager.getDefault();
        manager.newTask(new Task("Заняться спортом", "Совершить утреннюю пробежку", Status.NEW));
        manager.newEpic(new Epic("123", "321", Status.NEW));
        manager.newSubTask(new SubTask("sub", "info", Status.IN_PROGRESS, 2));
        HttpTaskManager newManager = HttpTaskManager.loadFromServer();
        System.out.println(newManager.getTask(1));
        System.out.println(newManager.getEpic(2));
        System.out.println(newManager.getSubTask(3));
    }
}
