package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.FileBackedTasksManager;
import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.manager.Manager;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @BeforeEach
    public void initInMemoryManager() {
        super.initManager(new FileBackedTasksManager("test/resources/TaskFile.csv"));
        taskManager.deleteAllTasks();
    }

    @Test
    public void shouldLoadEmptyTaskManagerFromEmptyCsvFile() {
        FileBackedTasksManager newTaskManager = FileBackedTasksManager.loadFromFile(
                Path.of("test/resources/TaskFile.csv").toFile()
        );
        List<Task> actualTaskList = new ArrayList<>();

        actualTaskList = newTaskManager.getAllTasks();

        assertEquals(new ArrayList<Task>(), actualTaskList);
    }

    @Test
    public void shouldSaveAndLoadEpicWithoutSubTasks() {


        taskManager.newEpic(epic);
        FileBackedTasksManager newTaskManager = FileBackedTasksManager.loadFromFile(
                Path.of("test/resources/TaskFile.csv").toFile()
        );

        assertEquals(epic.getId(), newTaskManager.getEpic(1).getId());
        assertEquals(epic.getName(), newTaskManager.getEpic(1).getName());
        assertEquals(epic.getInfo(), newTaskManager.getEpic(1).getInfo());
        assertEquals(epic.getStatus(), newTaskManager.getEpic(1).getStatus());
    }

    @Test
    public void shouldSaveAndReturnEmptyStringForEmptyHistory() {
        List<Task> expectedList = new ArrayList<>();
        List<Task> actualList;
        FileBackedTasksManager newTaskManager = FileBackedTasksManager.loadFromFile(
                Path.of("test/resources/TaskFile.csv").toFile()
        );

        taskManager.newTask(task);
        actualList = newTaskManager.getHistory();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void shouldReturnCorrectTimeFromTaskInfoFromCsvFile() {
        FileBackedTasksManager newTaskManager;
        LocalDateTime expectedStartTime = task.getStartTime();
        LocalDateTime expectedEndTime = task.getEndTime();
        Duration expectedDuration = task.getDuration();
        LocalDateTime actualStartTime;
        LocalDateTime actualEndTime;
        Duration actualDuration;

        taskManager.newTask(task);
        newTaskManager = FileBackedTasksManager.loadFromFile(
                Path.of("test/resources/TaskFile.csv").toFile()
        );
        actualStartTime = newTaskManager.getTask(1).getStartTime();
        actualEndTime = newTaskManager.getTask(1).getEndTime();
        actualDuration = newTaskManager.getTask(1).getDuration();

        assertEquals(expectedStartTime, actualStartTime);
        assertEquals(expectedDuration, actualDuration);
        assertEquals(expectedEndTime, actualEndTime);
    }

    @Test
    public void shouldReturnCorrectTimeFromEpicInfoFromCsvFile() {
        epic = new Epic("Epic", "test", Status.NEW);
        epic.setId(1);
        subTask = new SubTask("SubTask 1", "test", Status.NEW, epic, Duration.ofHours(3),
                LocalDateTime.from(task.getStartTime().plusHours(3)));
        FileBackedTasksManager newTaskManager;
        LocalDateTime expectedStartTime = subTask.getStartTime();
        LocalDateTime expectedEndTime = subTask.getEndTime();
        Duration expectedDuration = subTask.getDuration();
        LocalDateTime actualStartTime;
        LocalDateTime actualEndTime;
        Duration actualDuration;

        taskManager.newEpic(epic);
        taskManager.newSubTask(subTask);
        newTaskManager = FileBackedTasksManager.loadFromFile(
                Path.of("test/resources/TaskFile.csv").toFile()
        );
        actualStartTime = newTaskManager.getEpic(1).getStartTime();
        actualEndTime = newTaskManager.getEpic(1).getEndTime();
        actualDuration = newTaskManager.getEpic(1).getDuration();

        assertEquals(expectedStartTime, actualStartTime);
        assertEquals(expectedDuration, actualDuration);
        assertEquals(expectedEndTime, actualEndTime);
    }

    @Test
    public void shouldReturnCorrectTimeFromSubTaskInfoFromCsvFile() {
        epic = new Epic("Epic", "test", Status.NEW);
        epic.setId(1);
        subTask = new SubTask("SubTask 1", "test", Status.NEW, epic, Duration.ofHours(3),
                LocalDateTime.from(task.getStartTime().plusHours(3)));
        FileBackedTasksManager newTaskManager;
        LocalDateTime expectedStartTime = subTask.getStartTime();
        LocalDateTime expectedEndTime = subTask.getEndTime();
        Duration expectedDuration = subTask.getDuration();
        LocalDateTime actualStartTime;
        LocalDateTime actualEndTime;
        Duration actualDuration;

        taskManager.newEpic(epic);
        taskManager.newSubTask(subTask);
        newTaskManager = FileBackedTasksManager.loadFromFile(
                Path.of("test/resources/TaskFile.csv").toFile()
        );
        actualStartTime = newTaskManager.getSubTask(2).getStartTime();
        actualEndTime = newTaskManager.getSubTask(2).getEndTime();
        actualDuration = newTaskManager.getSubTask(2).getDuration();

        assertEquals(expectedStartTime, actualStartTime);
        assertEquals(expectedDuration, actualDuration);
        assertEquals(expectedEndTime, actualEndTime);
    }
}