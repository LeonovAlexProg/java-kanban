package ru.yandex.practicum.server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.manager.Manager;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.tasks.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HttpTaskServer {
    private HttpServer httpServer;

    private boolean isStarted = false;
    private final TaskManager fileBackedManager;

    public HttpTaskServer() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
            httpServer.createContext("/tasks", new TasksHandler());
        } catch (IOException exception) {
            System.out.println("Ошибка ввода вывода - " + exception.getMessage());
        }

        fileBackedManager = Manager.getFileBacked();
    }

    public TaskManager getFileBackedManager() {
        return fileBackedManager;
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public void startTaskServer() {
        if (!isStarted) {
            httpServer.start();
            isStarted = true;
            System.out.println("Сервер запущен на порту " + 8080);
        } else {
            System.out.println("Сервер уже запущен");
        }
    }

    class TasksHandler implements HttpHandler {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Task.class, new TaskSerializer())
                .registerTypeAdapter(Task.class, new TaskDeserializer())
                .registerTypeAdapter(Epic.class, new EpicSerializer())
                .registerTypeAdapter(Epic.class, new EpicDeserializer())
                .registerTypeAdapter(SubTask.class, new SubtaskSerializer())
                .registerTypeAdapter(SubTask.class, new SubtaskDeserializer())
                .create();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI uri = exchange.getRequestURI();
            Endpoint endpoint = getEndpoint(uri, exchange.getRequestMethod());
            String query = exchange.getRequestURI().getQuery();

            switch (endpoint) {
                case GET_TASK -> handleGetTask(exchange, query);
                case POST_TASK -> handlePostTask(exchange);
                case DELETE_TASK -> handleDeleteTask(exchange, query);
                case GET_EPIC -> handleGetEpic(exchange, query);
                case POST_EPIC -> handlePostEpic(exchange);
                case DELETE_EPIC -> handleDeleteEpic(exchange, query);
                case GET_SUBTASK -> handleGetSubtask(exchange, query);
                case POST_SUBTASK -> handlePostSubtask(exchange);
                case DELETE_SUBTASK -> handleDeleteSubtask(exchange, query);
                case GET_TASKS -> handleGetAllTasks(exchange);
                case GET_HISTORY -> handleGetHistory(exchange);
                case UNKNOWN -> writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        }

        public void handleGetTask(HttpExchange exchange, String query) throws IOException {
            int taskId = Integer.parseInt(query.split("=")[1]);

            Task task = fileBackedManager.getTask(taskId);

            if (task != null) {
                writeResponse(exchange, gson.toJson(task), 200);
            } else {
                writeResponse(exchange, "Task not found", 404);
            }
        }

        public void handlePostTask(HttpExchange exchange) throws IOException {
            String jsonString = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(jsonString, Task.class);
            int taskId = fileBackedManager.newTask(task);

            writeResponse(exchange, "Task has been added at index - " + taskId, 200);
        }

        public void handleDeleteTask(HttpExchange exchange, String query) throws IOException {
            int taskId = Integer.parseInt(query.split("=")[1]);

            if (fileBackedManager.getTask(taskId) != null) {
                fileBackedManager.deleteTaskById(taskId);
                writeResponse(exchange, "Task has been deleted", 200);
            } else {
                writeResponse(exchange, "Task not found", 404);
            }
        }

        public void handleGetEpic(HttpExchange exchange, String query) throws IOException {
            int epicId = Integer.parseInt(query.split("=")[1]);

            Epic epic = fileBackedManager.getEpic(epicId);

            if (epic != null) {
                writeResponse(exchange, gson.toJson(epic), 200);
            } else {
                writeResponse(exchange, "Task not found", 404);
            }
        }

        public void handlePostEpic(HttpExchange exchange) throws IOException {
            String jsonString = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(jsonString, Epic.class);
            int epicId = fileBackedManager.newEpic(epic);

            writeResponse(exchange, "Task has been added at index - " + epicId, 200);
        }

        public void handleDeleteEpic(HttpExchange exchange, String query) throws IOException {
            int epicId = Integer.parseInt(query.split("=")[1]);

            if (fileBackedManager.getEpic(epicId) != null) {
                fileBackedManager.deleteEpicById(epicId);
                writeResponse(exchange, "Task has been deleted", 200);
            } else {
                writeResponse(exchange, "Task not found", 404);
            }
        }

        public void handleGetSubtask(HttpExchange exchange, String query) throws IOException {
            int subtaskId = Integer.parseInt(query.split("=")[1]);

            SubTask subtask = fileBackedManager.getSubTask(subtaskId);

            if (subtask != null) {
                writeResponse(exchange, gson.toJson(subtask), 200);
            } else {
                writeResponse(exchange, "Task not found", 404);
            }
        }

        public void handlePostSubtask(HttpExchange exchange) throws IOException {
            String jsonString = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            SubTask subtask = gson.fromJson(jsonString, SubTask.class);
            int subtaskId = fileBackedManager.newSubTask(subtask);

            writeResponse(exchange, "Task has been added at index - " + subtaskId, 200);
        }

        public void handleDeleteSubtask(HttpExchange exchange, String query) throws IOException {
            int subtaskId = Integer.parseInt(query.split("=")[1]);

            if (fileBackedManager.getSubTask(subtaskId) != null) {
                fileBackedManager.deleteSubTaskById(subtaskId);
                writeResponse(exchange, "Task has been deleted", 200);
            } else {
                writeResponse(exchange, "Task not found", 404);
            }
        }

        public void handleGetAllTasks(HttpExchange exchange) throws IOException {
            List<Task> allTasks = fileBackedManager.getAllTasks();
            String jsonString = gson.toJson(allTasks);

            if (jsonString.isEmpty()) {
                writeResponse(exchange, "tasks not found", 404);
            } else {
                writeResponse(exchange, jsonString, 200);
            }
        }

        public void handleGetHistory(HttpExchange exchange) throws IOException {
            List<Task> history = fileBackedManager.getHistory();
            String jsonHistory = gson.toJson(history);

            if (jsonHistory.isEmpty()) {
                writeResponse(exchange, "history is empty", 404);
            } else {
                writeResponse(exchange, jsonHistory, 200);
            }
        }

        private Endpoint getEndpoint(URI uri, String requestMethod) {
            String query = uri.getQuery();
            String[] pathParts = uri.getPath().split("/");

            if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                return Endpoint.GET_TASKS;
            }
            if (pathParts.length == 3 && pathParts[1].equals("tasks") && pathParts[2].equals("history")) {
                return Endpoint.GET_HISTORY;
            }
            if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
                if (pathParts[2].equals("task")) {

                    if (requestMethod.equals("POST")) {
                        return Endpoint.POST_TASK;
                    }
                    if (query != null) {
                        if (requestMethod.equals("GET")) {
                            return Endpoint.GET_TASK;
                        }
                        if (requestMethod.equals("DELETE")) {
                            return Endpoint.DELETE_TASK;
                        }
                    }
                }
                if (pathParts[2].equals("epic")) {

                    if (requestMethod.equals("POST")) {
                        return Endpoint.POST_EPIC;
                    }
                    if (query != null) {
                        if (requestMethod.equals("GET")) {
                            return Endpoint.GET_EPIC;
                        }
                        if (requestMethod.equals("DELETE")) {
                            return Endpoint.DELETE_EPIC;
                        }
                    }
                }
                if (pathParts[2].equals("subtask")) {
                    if (requestMethod.equals("POST")) {
                        return Endpoint.POST_SUBTASK;
                    }
                    if (query != null) {
                        if (requestMethod.equals("GET")) {
                            return Endpoint.GET_SUBTASK;
                        }
                        if (requestMethod.equals("DELETE")) {
                            return Endpoint.DELETE_SUBTASK;
                        }
                    }
                }
            }
            return Endpoint.UNKNOWN;
        }

        private void writeResponse(HttpExchange exchange,
                                   String responseString,
                                   int responseCode) throws IOException {
            if(responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(StandardCharsets.UTF_16);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }

        public class TaskSerializer implements JsonSerializer<Task> {
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

        public class TaskDeserializer implements JsonDeserializer<Task> {
            @Override
            public Task deserialize(JsonElement jsonElement, Type type,
                                    JsonDeserializationContext jsonDeserializationContext)
                    throws JsonParseException {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Task newTask = new Task(jsonObject.get("name").getAsString(), jsonObject.get("info").getAsString(),
                        Status.valueOf(jsonObject.get("status").getAsString()));

                if (jsonObject.has("start time") && jsonObject.has("duration")) {
                    newTask.setStartTime(LocalDateTime.parse(jsonObject.get("start time").getAsString(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")));
                    newTask.setDuration(Duration.parse(jsonObject.get("duration").getAsString()));
                }

                return newTask;
            }
        }

        public class EpicSerializer implements JsonSerializer<Epic> {
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

        public class EpicDeserializer implements JsonDeserializer<Epic> {
            @Override
            public Epic deserialize(JsonElement jsonElement, Type type,
                                    JsonDeserializationContext jsonDeserializationContext)
                    throws JsonParseException {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Epic newEpic = new Epic(jsonObject.get("name").getAsString(), jsonObject.get("info").getAsString(),
                        Status.valueOf(jsonObject.get("status").getAsString()));

                if (jsonObject.has("start time") && jsonObject.has("duration")) {
                    newEpic.setStartTime(LocalDateTime.parse(jsonObject.get("start time").getAsString(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")));
                    newEpic.setDuration(Duration.parse(jsonObject.get("duration").getAsString()));
                }

                return newEpic;
            }
        }

        public class SubtaskSerializer implements JsonSerializer<SubTask> {
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

        public class SubtaskDeserializer implements JsonDeserializer<SubTask> {
            @Override
            public SubTask deserialize(JsonElement jsonElement, Type type,
                                       JsonDeserializationContext jsonDeserializationContext)
                    throws JsonParseException {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                SubTask newSubtask = new SubTask(jsonObject.get("name").getAsString(),
                        jsonObject.get("info").getAsString(),
                        Status.valueOf(jsonObject.get("status").getAsString()),
                        fileBackedManager.getEpic(jsonObject.get("epic").getAsInt()));

                if (jsonObject.has("start time") && jsonObject.has("duration")) {
                    newSubtask.setStartTime(LocalDateTime.parse(jsonObject.get("start time").getAsString(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")));
                    newSubtask.setDuration(Duration.parse(jsonObject.get("duration").getAsString()));
                }

                return newSubtask;
            }
        }
    }
}
