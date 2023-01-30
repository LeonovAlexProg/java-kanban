package tasks;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.manager.Manager;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.TaskTypes;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    TaskManager manager;
    Epic epic;
    int epicId;
    List<SubTask> subTasks;

    @BeforeEach
    public void initManager() {
        manager = Manager.getDefault();

        epicId = manager.newEpic(
                new Epic("TestEpic", "Epic for testing", Status.NEW)
        );

        epic = manager.getEpic(epicId);

        subTasks = List.of(
                new SubTask("subtask1", "test subtask", Status.NEW, manager.getEpic(epicId)),
                new SubTask("subtask2", "test subtask", Status.NEW, manager.getEpic(epicId)),
                new SubTask("subtask3", "test subtask", Status.NEW, manager.getEpic(epicId))
        );
    }

    @AfterEach
    public void clearCsv() {
        try (FileWriter fw = new FileWriter(Paths.get("test/resources/TaskFile.csv").toFile(),false)){
        } catch (IOException exc) {
            exc.getCause();
        }
    }

    @Test
    public void shouldReturnEpicWithNewStatusWithEmptySubtasksList() {
        Status expectedEpicStatus = Status.NEW;
        Status actualEpicStatus;

        actualEpicStatus = manager.getEpic(epicId).getStatus();

        assertEquals(expectedEpicStatus, actualEpicStatus);
    }

    @Test
    public void shouldReturnEpicWithNewStatusWithSubtasksListWhereAllNew() {
        for (SubTask subtask : subTasks) {
            manager.newSubTask(subtask);
        }

        Status expectedEpicStatus = Status.NEW;
        Status actualEpicStatus;

        actualEpicStatus = manager.getEpic(epicId).getStatus();

        assertEquals(expectedEpicStatus, actualEpicStatus);
    }

    @Test
    public void shouldReturnEpicWithDoneStatusWithSubtasksListWhereAllDone() {
        for (SubTask subtask : subTasks) {
            subtask.setStatus(Status.DONE);
            manager.newSubTask(subtask);
        }

        Status expectedEpicStatus = Status.DONE;
        Status actualEpicStatus;

        actualEpicStatus = manager.getEpic(epicId).getStatus();

        assertEquals(expectedEpicStatus, actualEpicStatus);
    }

    @Test
    public void shouldReturnEpicWithInProgressStatusWithSubtasksListWhereNewAndDone() {
        subTasks.get(0).setStatus(Status.NEW);
        subTasks.get(1).setStatus(Status.DONE);
        subTasks.get(2).setStatus(Status.DONE);

        for (SubTask subtask : subTasks) {
            manager.newSubTask(subtask);
        }

        Status expectedEpicStatus = Status.IN_PROGRESS;
        Status actualEpicStatus;

        actualEpicStatus = manager.getEpic(epicId).getStatus();

        assertEquals(expectedEpicStatus, actualEpicStatus);
    }

    @Test
    public void shouldReturnEpicWIthInProgressStatusWithSubtasksListWhereAllInProgress() {
        for (SubTask subtask : subTasks) {
            subtask.setStatus(Status.IN_PROGRESS);
            manager.newSubTask(subtask);
        }

        Status expectedEpicStatus = Status.IN_PROGRESS;
        Status actualEpicStatus;

        actualEpicStatus = manager.getEpic(epicId).getStatus();

        assertEquals(expectedEpicStatus, actualEpicStatus);
    }

    @Test
    public void shouldReturnCorrectStringFromNewEpicToStringMethod() {

        String expectedString = "Epic{subTasks=[], name='TestEpic', info='Epic for testing', id=1, status='NEW'}\n";
        String actualString;

        actualString = manager.getEpic(epicId).toString();

        assertEquals(expectedString, actualString);
        System.out.println(epic.toString());
    }

    @Test
    public void shouldReturnCorrectCsvStringFromNewEpicToCsvStringMethod() {
        String expectedCsvString = String.format(
                "%d,%s,%s,%s,%s,%n",
                epic.getId(),
                TaskTypes.EPIC,
                epic.getName(),
                epic.getStatus(),
                epic.getInfo()
        );
        String actualCsvString;

        actualCsvString = manager.getEpic(epicId).toCsvString();

        assertEquals(expectedCsvString, actualCsvString);
    }
}