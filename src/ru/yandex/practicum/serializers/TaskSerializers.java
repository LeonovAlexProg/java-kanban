package ru.yandex.practicum.serializers;

import com.google.gson.*;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskSerializers {
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
            }

            if (src.getDuration() != null) {
                result.addProperty("duration", src.getDuration().toString());
            }

            if (src.getEndTime() != null) {
                result.addProperty("end time", src.getEndTime().toString());
            }

            return result;
        }
    }

    public static class TaskDeserializer implements JsonDeserializer<Task> {
        @Override
        public Task deserialize(JsonElement jsonElement, Type type,
                                JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Task newTask = new Task(jsonObject.get("name").getAsString(), jsonObject.get("info").getAsString(),
                    Status.valueOf(jsonObject.get("status").getAsString()));

            if (jsonObject.has("id")) {
                newTask.setId(jsonObject.get("id").getAsInt());
            }

            if (jsonObject.has("start time") && jsonObject.has("duration")) {
                newTask.setStartTime(LocalDateTime.parse(jsonObject.get("start time").getAsString(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")));
                newTask.setDuration(Duration.parse(jsonObject.get("duration").getAsString()));
            }

            return newTask;
        }
    }

    public static class EpicSerializer implements JsonSerializer<Epic> {
        @Override
        public JsonElement serialize(Epic src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();

            result.addProperty("name", src.getName());
            result.addProperty("info", src.getInfo());
            result.addProperty("id", src.getId());
            result.addProperty("status", src.getStatus().toString());
            if (src.getStartTime() != null) {
                result.addProperty("start time", src.getStartTime().toString());
            }

            if (src.getDuration() != null) {
                result.addProperty("duration", src.getDuration().toString());
            }

            if (src.getEndTime() != null) {
                result.addProperty("end time", src.getEndTime().toString());
            }

            return result;
        }
    }

    public static class EpicDeserializer implements JsonDeserializer<Epic> {
        @Override
        public Epic deserialize(JsonElement jsonElement, Type type,
                                JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Epic newEpic = new Epic(jsonObject.get("name").getAsString(), jsonObject.get("info").getAsString(),
                    Status.valueOf(jsonObject.get("status").getAsString()));

            if (jsonObject.has("id")) {
                newEpic.setId(jsonObject.get("id").getAsInt());
            }

            if (jsonObject.has("start time") && jsonObject.has("duration")) {
                newEpic.setStartTime(LocalDateTime.parse(jsonObject.get("start time").getAsString(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")));
                newEpic.setDuration(Duration.parse(jsonObject.get("duration").getAsString()));
            }

            return newEpic;
        }
    }

    public static class SubtaskSerializer implements JsonSerializer<SubTask> {
        @Override
        public JsonElement serialize(SubTask src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();

            result.addProperty("name", src.getName());
            result.addProperty("info", src.getInfo());
            result.addProperty("id", src.getId());
            result.addProperty("status", src.getStatus().toString());
            result.addProperty("epic", src.getEpicId());
            if (src.getStartTime() != null) {
                result.addProperty("start time", src.getStartTime().toString());
            }

            if (src.getDuration() != null) {
                result.addProperty("duration", src.getDuration().toString());
            }

            if (src.getEndTime() != null) {
                result.addProperty("end time", src.getEndTime().toString());
            }

            return result;
        }
    }

    public static class SubtaskDeserializer implements JsonDeserializer<SubTask> {
        @Override
        public SubTask deserialize(JsonElement jsonElement, Type type,
                                   JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            SubTask newSubtask = new SubTask(jsonObject.get("name").getAsString(),
                    jsonObject.get("info").getAsString(),
                    Status.valueOf(jsonObject.get("status").getAsString()),
                    jsonObject.get("epic").getAsInt());

            if (jsonObject.has("id")) {
                newSubtask.setId(jsonObject.get("id").getAsInt());
            }

            if (jsonObject.has("start time") && jsonObject.has("duration")) {
                newSubtask.setStartTime(LocalDateTime.parse(jsonObject.get("start time").getAsString(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")));
                newSubtask.setDuration(Duration.parse(jsonObject.get("duration").getAsString()));
            }

            return newSubtask;
        }
    }
}
