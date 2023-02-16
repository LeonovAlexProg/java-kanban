package servers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.serializers.TaskSerializers;
import ru.yandex.practicum.servers.httptaskserver.HttpTaskServer;
import ru.yandex.practicum.servers.kvserver.KVServer;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HttpTaskServerTest {
    HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    String url = "http://localhost:8080/tasks/";
    Gson gson;
    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    HttpClient client;

    Task task = new Task("Заняться спортом", "Совершить утреннюю пробежку", Status.NEW);
    Epic epic = new Epic("123", "321", Status.NEW);
    SubTask subTask = new SubTask("sub", "info", Status.IN_PROGRESS, 2);

    @BeforeEach
    public void startServers() {
         task.setId(1);
         epic.setId(2);
        try {
            kvServer = new KVServer();
        } catch (IOException exception) {
            exception.getMessage();
        }
        kvServer.start();
         subTask.setId(3);
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.startTaskServer();
        client = HttpClient.newHttpClient();

        gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskSerializers.TaskSerializer())
                .registerTypeAdapter(Task.class, new TaskSerializers.TaskDeserializer())
                .registerTypeAdapter(Epic.class, new TaskSerializers.EpicSerializer())
                .registerTypeAdapter(Epic.class, new TaskSerializers.EpicDeserializer())
                .registerTypeAdapter(SubTask.class, new TaskSerializers.SubtaskSerializer())
                .registerTypeAdapter(SubTask.class, new TaskSerializers.SubtaskDeserializer())
                .create();


    }

    @AfterEach
    public void stopServers() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void postAndGetTaskTest() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher;
        HttpRequest postRequest;
        HttpRequest getRequest;
        HttpResponse<String> response;
        Task expectedTask;
        Task actualTask;

        expectedTask = task;
        URI getUri = URI.create(url + "task?id=1");
        URI postUri = URI.create(url + "task");
        bodyPublisher = HttpRequest.BodyPublishers.ofString(gson.toJson(task), StandardCharsets.UTF_8);
        postRequest = HttpRequest.newBuilder().POST(bodyPublisher).uri(postUri).build();
        getRequest = HttpRequest.newBuilder().GET().uri(getUri).build();
        client.send(postRequest, handler);
        response = client.send(getRequest, handler);
        actualTask = gson.fromJson(response.body(), Task.class);

        Assertions.assertEquals(expectedTask, actualTask);
    }

    @Test
    void deleteTaskTest() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher;
        HttpRequest postRequest;
        HttpRequest getRequest;
        HttpRequest deleteRequest;
        HttpResponse<String> response;
        Task expectedTask;
        Task actualTask;

        expectedTask = task;
        URI getAndDeleteUri = URI.create(url + "task?id=1");
        URI postUri = URI.create(url + "task");
        bodyPublisher = HttpRequest.BodyPublishers.ofString(gson.toJson(task), StandardCharsets.UTF_8);
        postRequest = HttpRequest.newBuilder().POST(bodyPublisher).uri(postUri).build();
        deleteRequest = HttpRequest.newBuilder().DELETE().uri(getAndDeleteUri).build();
        getRequest = HttpRequest.newBuilder().GET().uri(getAndDeleteUri).build();
        client.send(postRequest, handler);
        client.send(deleteRequest, handler);
        response = client.send(getRequest, handler);
        actualTask = gson.fromJson(response.body().toString(), Task.class);

        Assertions.assertEquals("Task not found", actualTask);
    }

    @Test
    void postAndGetEpicTest() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher;
        HttpRequest postRequest;
        HttpRequest getRequest;
        HttpResponse<String> response;
        Epic expectedEpic;
        Epic actualEpic;

        expectedEpic = epic;
        URI getUri = URI.create(url + "epic?id=1");
        URI postUri = URI.create(url + "epic");
        bodyPublisher = HttpRequest.BodyPublishers.ofString(gson.toJson(epic), StandardCharsets.UTF_8);
        postRequest = HttpRequest.newBuilder().POST(bodyPublisher).uri(postUri).build();
        getRequest = HttpRequest.newBuilder().GET().uri(getUri).build();
        client.send(postRequest, handler);
        response = client.send(getRequest, handler);
        actualEpic = gson.fromJson(response.body(), Epic.class);

        Assertions.assertEquals(expectedEpic, actualEpic);
    }

    @Test
    void deleteEpicTest() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher;
        HttpRequest postRequest;
        HttpRequest getRequest;
        HttpRequest deleteRequest;
        HttpResponse<String> response;
        Epic expectedEpic;
        Task actualEpic;

        expectedEpic = epic;
        URI getAndDeleteUri = URI.create(url + "epic?id=1");
        URI postUri = URI.create(url + "epic");
        bodyPublisher = HttpRequest.BodyPublishers.ofString(gson.toJson(epic), StandardCharsets.UTF_8);
        postRequest = HttpRequest.newBuilder().POST(bodyPublisher).uri(postUri).build();
        deleteRequest = HttpRequest.newBuilder().DELETE().uri(getAndDeleteUri).build();
        getRequest = HttpRequest.newBuilder().GET().uri(getAndDeleteUri).build();
        client.send(postRequest, handler);
        client.send(deleteRequest, handler);
        response = client.send(getRequest, handler);
        actualEpic = gson.fromJson(response.body().toString(), Task.class);

        Assertions.assertEquals("Task not found", actualEpic);
    }

    @Test
    void postAndGetSubtaskTest() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher;
        HttpRequest postEpicRequest;
        HttpRequest postRequest;
        HttpRequest getRequest;
        HttpResponse<String> response;
        SubTask expectedSubtask;
        SubTask actualSubtask;

        expectedSubtask = subTask;
        expectedSubtask.setEpicId(1);
        URI getUri = URI.create(url + "subtask?id=2");
        URI postEpicUri = URI.create(url + "epic");
        URI postUri = URI.create(url + "subtask");
        bodyPublisher = HttpRequest.BodyPublishers.ofString(gson.toJson(subTask), StandardCharsets.UTF_8);
        postEpicRequest = HttpRequest.newBuilder().POST(
                HttpRequest.BodyPublishers.ofString(gson.toJson(epic), StandardCharsets.UTF_8)
        ).uri(postEpicUri).build();
        postRequest = HttpRequest.newBuilder().POST(bodyPublisher).uri(postUri).build();
        getRequest = HttpRequest.newBuilder().GET().uri(getUri).build();
        client.send(postEpicRequest, handler);
        client.send(postRequest, handler);
        response = client.send(getRequest, handler);
        actualSubtask = gson.fromJson(response.body(), SubTask.class);

        Assertions.assertEquals(expectedSubtask, actualSubtask);
    }

    @Test
    void deleteSubtaskTest() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher;
        HttpRequest postEpicRequest;
        HttpRequest postRequest;
        HttpRequest getRequest;
        HttpResponse<String> response;
        SubTask expectedSubtask;
        SubTask actualSubtask;

        expectedSubtask = subTask;
        expectedSubtask.setEpicId(1);
        URI getUri = URI.create(url + "subtask?id=2");
        URI postEpicUri = URI.create(url + "epic");
        URI postUri = URI.create(url + "subtask");
        bodyPublisher = HttpRequest.BodyPublishers.ofString(gson.toJson(subTask), StandardCharsets.UTF_8);
        postEpicRequest = HttpRequest.newBuilder().POST(
                HttpRequest.BodyPublishers.ofString(gson.toJson(epic), StandardCharsets.UTF_8)
        ).uri(postEpicUri).build();
        postRequest = HttpRequest.newBuilder().POST(bodyPublisher).uri(postUri).build();
        getRequest = HttpRequest.newBuilder().GET().uri(getUri).build();
        client.send(postEpicRequest, handler);
        client.send(postRequest, handler);
        response = client.send(getRequest, handler);
        actualSubtask = gson.fromJson(response.body(), SubTask.class);

        Assertions.assertEquals("Task not found", actualSubtask);
    }

    @Test
    void testGetTasks() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher taskBodyPublisher;
        HttpRequest.BodyPublisher epicBodyPublisher;
        HttpRequest.BodyPublisher subtaskBodyPublisher;
        HttpRequest postEpicRequest;
        HttpRequest postTaskRequest;
        HttpRequest postSubtaskRequest;
        HttpRequest getRequest;
        HttpResponse<String> response;

        URI getUri = URI.create(url);
        URI postEpicUri = URI.create(url + "epic");
        URI postTaskUri = URI.create(url + "task");
        URI postSubtaskUri = URI.create(url + "subtask");
        taskBodyPublisher = HttpRequest.BodyPublishers.ofString(gson.toJson(task), StandardCharsets.UTF_8);
        epicBodyPublisher = HttpRequest.BodyPublishers.ofString(gson.toJson(epic), StandardCharsets.UTF_8);
        subtaskBodyPublisher = HttpRequest.BodyPublishers.ofString(gson.toJson(subTask), StandardCharsets.UTF_8);
        postTaskRequest = HttpRequest.newBuilder().POST(taskBodyPublisher).uri(postTaskUri).build();
        postEpicRequest = HttpRequest.newBuilder().POST(epicBodyPublisher).uri(postEpicUri).build();
        postSubtaskRequest = HttpRequest.newBuilder().POST(subtaskBodyPublisher).uri(postSubtaskUri).build();
        getRequest = HttpRequest.newBuilder().GET().uri(getUri).build();
        client.send(postTaskRequest, handler);
        client.send(postEpicRequest, handler);
        client.send(postSubtaskRequest, handler);

        response = client.send(getRequest, handler);
        System.out.println(response.body());
    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {
        int taskId = httpTaskServer.getTaskManager().newTask(task);
        int epicId = httpTaskServer.getTaskManager().newEpic(epic);
        int subtaskId = httpTaskServer.getTaskManager().newSubTask(subTask);

        httpTaskServer.getTaskManager().getTask(1);
        httpTaskServer.getTaskManager().getTask(2);
        httpTaskServer.getTaskManager().getTask(3);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, handler);

        System.out.println(response.body());
    }
}
