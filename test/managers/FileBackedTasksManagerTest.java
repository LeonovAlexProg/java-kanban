package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.FileBackedTasksManager;
import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.manager.Manager;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.tasks.Task;

import java.nio.file.Path;
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
}