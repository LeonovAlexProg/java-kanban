package ru.yandex.practicum.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.serializers.TaskSerializers;
import ru.yandex.practicum.servers.client.KVTaskClient;
import ru.yandex.practicum.servers.httptaskserver.HttpTaskServer;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager{
    Gson gson;
    URI serverUrl;
    KVTaskClient taskClient;

    public HttpTaskManager(String url) {
        super();
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Task.class, new TaskSerializers.TaskSerializer())
                .registerTypeAdapter(Task.class, new TaskSerializers.TaskDeserializer())
                .registerTypeAdapter(Epic.class, new TaskSerializers.EpicSerializer())
                .registerTypeAdapter(Epic.class, new TaskSerializers.EpicDeserializer())
                .registerTypeAdapter(SubTask.class, new TaskSerializers.SubtaskSerializer())
                .registerTypeAdapter(SubTask.class, new TaskSerializers.SubtaskDeserializer())
                .create();

        serverUrl = URI.create(url);
        try {
            taskClient = new KVTaskClient(url);
        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    protected void save() {
        try {
            taskClient.put("history", gson.toJson(getHistory()));
            taskClient.put("tasks", gson.toJson(tasks.values()));
            taskClient.put("epics", gson.toJson(epics.values()));
            taskClient.put("subtasks", gson.toJson(subTasks.values()));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFromServer() {
        try {
            String tasks = taskClient.load("tasks");
            String epics = taskClient.load("epics");
            String subtasks = taskClient.load("subtasks");
            String history = taskClient.load("history");

            System.out.println(tasks);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
