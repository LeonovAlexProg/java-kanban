package ru.yandex.practicum.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.serializers.TaskSerializers;
import ru.yandex.practicum.servers.client.KVTaskClient;
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

    public static HttpTaskManager loadFromServer() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Task.class, new TaskSerializers.TaskSerializer())
                .registerTypeAdapter(Task.class, new TaskSerializers.TaskDeserializer())
                .registerTypeAdapter(Epic.class, new TaskSerializers.EpicSerializer())
                .registerTypeAdapter(Epic.class, new TaskSerializers.EpicDeserializer())
                .registerTypeAdapter(SubTask.class, new TaskSerializers.SubtaskSerializer())
                .registerTypeAdapter(SubTask.class, new TaskSerializers.SubtaskDeserializer())
                .create();
        HttpTaskManager httpTaskManager = (HttpTaskManager) Manager.getDefault();

        try {
            String tasks = httpTaskManager.taskClient.load("tasks");
            String epics = httpTaskManager.taskClient.load("epics");
            String subtasks = httpTaskManager.taskClient.load("subtasks");
            String history = httpTaskManager.taskClient.load("history");

            List<Task> tasksFromJson;
            List<Epic> epicsFromJson;
            List<SubTask> subtasksFromJson;
            List<Integer> historyFromString = null;

            if (!history.equals("[]")) {
                historyFromString = FileBackedTasksManager.historyFromString(history);
            }

            if (!tasks.equals("[]")) {
                tasksFromJson = gson.fromJson(tasks, new TypeToken<List<Task>>() {}.getType());
                for (Task task : tasksFromJson) {
                    httpTaskManager.tasks.put(task.getId(), task);
                    if (historyFromString != null && historyFromString.contains(task.getId())) {
                        httpTaskManager.historyManager.add(task);
                    }
                    if (httpTaskManager.id < task.getId()) {
                        httpTaskManager.id = task.getId();
                    }
                }
            }
            if (!epics.equals("[]")) {
                epicsFromJson = gson.fromJson(epics, new TypeToken<List<Epic>>() {
                }.getType());

                for (Epic epic : epicsFromJson) {
                    httpTaskManager.epics.put(epic.getId(), epic);
                    if (historyFromString != null && historyFromString.contains(epic.getId())) {
                        httpTaskManager.historyManager.add(epic);
                    }
                    if (httpTaskManager.id < epic.getId()) {
                        httpTaskManager.id = epic.getId();
                    }
                }
            }
            if (!subtasks.equals("[]")) {
                subtasksFromJson = gson.fromJson(subtasks, new TypeToken<List<SubTask>>() {
                }.getType());

                for (SubTask subtask : subtasksFromJson) {
                    httpTaskManager.subTasks.put(subtask.getId(), subtask);
                    if (historyFromString != null && historyFromString.contains(subtask.getId())) {
                        httpTaskManager.historyManager.add(subtask);
                    }
                    if (httpTaskManager.id < subtask.getId()) {
                        httpTaskManager.id = subtask.getId();
                    }
                }
            }

            return httpTaskManager;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
