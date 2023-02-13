package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.InMemoryHistoryManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Task 1", "test", Status.NEW);
        epic = new Epic("Epic", "test", Status.NEW);
        epic.setId(-1);
        subTask = new SubTask("SubTask 1", "test", Status.NEW, epic.getId());
    }

    @Test
    void shouldReturnEmptyHistory() {
        List<Task> expectedHistory = new ArrayList<>();
        List<Task> actualHistory;

        actualHistory = historyManager.getHistory();

        assertEquals(expectedHistory, actualHistory);
    }

    @Test
    void shouldNotReturnTwoSameTasksInARow() {
        List<Task> expectedHistory = List.of(
                epic,
                task
        );
        List<Task> actualList;

        task.setId(1);
        historyManager.add(task);
        historyManager.add(task);
        epic.setId(2);
        historyManager.add(epic);
        historyManager.add(task);
        actualList = historyManager.getHistory();

        assertArrayEquals(expectedHistory.toArray(), actualList.toArray());
    }

    @Test
    void shouldDeleteTaskFromTheBeginningOfHistory() {
        List<Task> expectedHistory = List.of(
                epic, subTask
        );
        List<Task> actualList;

        task.setId(1);
        historyManager.add(task);
        epic.setId(2);
        historyManager.add(epic);
        subTask.setId(3);
        subTask.setEpicId(2);
        historyManager.add(subTask);
        historyManager.remove(1);
        actualList = historyManager.getHistory();

        assertArrayEquals(expectedHistory.toArray(), actualList.toArray());
    }

    @Test
    void shouldDeleteTaskFromTheMiddleOfHistory() {
        List<Task> expectedHistory = List.of(
                task, subTask
        );
        List<Task> actualList;

        task.setId(1);
        historyManager.add(task);
        epic.setId(2);
        historyManager.add(epic);
        subTask.setId(3);
        subTask.setEpicId(2);
        historyManager.add(subTask);
        historyManager.remove(2);
        actualList = historyManager.getHistory();

        assertArrayEquals(expectedHistory.toArray(), actualList.toArray());
    }

    @Test
    void shouldDeleteTaskFromTheEndOfHistory() {
        List<Task> expectedHistory = List.of(
                task, epic
        );
        List<Task> actualList;

        task.setId(1);
        historyManager.add(task);
        epic.setId(2);
        historyManager.add(epic);
        subTask.setId(3);
        subTask.setEpicId(2);
        historyManager.add(subTask);
        historyManager.remove(3);
        actualList = historyManager.getHistory();

        assertArrayEquals(expectedHistory.toArray(), actualList.toArray());
    }
}