package ru.yandex.practicum.servers.kvserver;

import ru.yandex.practicum.manager.HttpTaskManager;
import ru.yandex.practicum.servers.client.KVTaskClient;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.Task;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();

        HttpTaskManager manager = new HttpTaskManager("localhost:8081/");
        manager.newTask(new Task("Заняться спортом", "Совершить утреннюю пробежку", Status.NEW));
    }
}
