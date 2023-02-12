package ru.yandex.practicum.servers.httptaskserver;

import com.google.gson.*;

import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {
        HttpTaskServer server = new HttpTaskServer();
        server.startTaskServer();

        Task firstTask = new Task("Заняться спортом", "Совершить утреннюю пробежку", Status.NEW,
                Duration.ofHours(3), LocalDateTime.now());

        int id = server.getFileBackedManager().newTask(firstTask);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(Task.class, new TaskSerializer())
                .create();

        System.out.println(gson.toJson(server.getFileBackedManager().getTask(id)));
    }

    public static class TaskSerializer implements JsonSerializer<Task> {
        @Override
        public JsonElement serialize(Task src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();

            result.addProperty("name", src.getName());
            result.addProperty("info", src.getInfo());
            result.addProperty("id", src.getId());
            result.addProperty("status", src.getStatus().toString());
            if (src.getStartTime() != null) {
                result.addProperty("start time", src.getStartTime().toString());
            } else {
                result.addProperty("start time", "null");
            }

            if (src.getDuration() != null) {
                result.addProperty("duration", src.getDuration().toString());
            } else {
                result.addProperty("duration", "null");
            }

            if (src.getEndTime() != null) {
                result.addProperty("end time", src.getEndTime().toString());
            } else {
                result.addProperty("end time", "null");
            }

            return result;
        }
    }
}
