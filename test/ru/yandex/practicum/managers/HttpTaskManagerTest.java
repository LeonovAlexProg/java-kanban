package ru.yandex.practicum.managers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.servers.kvserver.KVServer;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.io.IOException;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    TaskManager httpTaskManager;
    KVServer kvServer;
    Task task;
    Epic epic;
    SubTask subtask;

    @BeforeEach
    public void startServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();

        super.initManager((HttpTaskManager) Manager.getDefault());

        task = new Task("Заняться спортом", "Совершить утреннюю пробежку", Status.NEW);
        epic = new Epic("Сходить в магазин", "Закупиться продуктами по списку", Status.NEW);
    }

    @AfterEach
    public void stopServer() {
        kvServer.stop();
    }

    @Test
    void shouldLoadAndReturnTasks() {
        int taskId;
        int epicId;
        int subtaskId;
        Task expectedTask;
        Epic expectedEpic;
        SubTask expectedSubtask;


        taskId = taskManager.newTask(task);
        epicId = taskManager.newEpic(epic);
        subtaskId = taskManager.newSubTask(new SubTask("Подготовить список", "подготовить список того что нужно купить",
                Status.NEW, epic.getId()));
        HttpTaskManager newManager = HttpTaskManager.loadFromServer();
        expectedTask = newManager.getTask(1);
        expectedEpic = newManager.getEpic(2);
        expectedSubtask = newManager.getSubTask(3);

        Assertions.assertEquals(task, expectedTask);
        Assertions.assertEquals(epic, expectedEpic);
        Assertions.assertEquals(new SubTask("Подготовить список", "подготовить список того что нужно купить",
                Status.NEW, epic.getId()), expectedSubtask);
    }
}
