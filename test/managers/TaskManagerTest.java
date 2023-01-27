package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
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
import java.util.Set;
import java.util.TreeSet;

abstract class TaskManagerTest<T extends TaskManager> {
    LocalDateTime actualDateTime = LocalDateTime.now();
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;

    public void initManager(T taskManager) {
        this.taskManager = taskManager;
    }

    @BeforeEach
    void setUpTasks() {
        task = new Task("Task 1", "test", Status.NEW, Duration.ofHours(2),
                LocalDateTime.from(actualDateTime));
        epic = new Epic("Epic", "test", Status.NEW);
        epic.setId(-1);
        subTask = new SubTask("SubTask 1", "test", Status.NEW, epic, Duration.ofHours(3),
                LocalDateTime.from(task.getStartTime().plusHours(3)));
    }

    @Test
    public void getAllTasksMethodWithEmptyTasks() {
        ArrayList<Task> expectedList = new ArrayList<>();
        ArrayList<Task> actualList;

        actualList = taskManager.getAllTasks();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void getAllTasksMethodWithAllTypeOfTasks() {
        ArrayList<Task> expectedList = new ArrayList<>() {{
            this.add(task);
            this.add(epic);
            this.add(subTask);
        }};
        ArrayList<Task> actualList;

        taskManager.newTask(task);
        epic.setId(2);
        taskManager.newEpic(epic);
        subTask.setEpicId(2);
        taskManager.newSubTask(subTask);
        actualList = taskManager.getAllTasks();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void getTasksMethodWithEmptyTasks() {
        ArrayList<Task> expectedList = new ArrayList<>();
        List<Task> actualList;

        actualList = taskManager.getTasks();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void getTasksMethodWithTasks() {
        ArrayList<Task> expectedList = new ArrayList<>() {{
            this.add(task);
            this.add(task);
            this.add(task);
        }};
        List<Task> actualList;

        taskManager.newTask(task);
        taskManager.newTask(task);
        taskManager.newTask(task);
        actualList = taskManager.getTasks();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void getEpicsMethodWithEmptyEpics() {
        ArrayList<Epic> expectedList = new ArrayList<>();
        List<Epic> actualList;

        actualList = taskManager.getEpics();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void getEpicsMethodWithEpics() {
        ArrayList<Epic> expectedList = new ArrayList<>() {{
            this.add(epic);
            this.add(epic);
            this.add(epic);
        }};
       List<Epic> actualList;

        taskManager.newEpic(epic);
        taskManager.newEpic(epic);
        taskManager.newEpic(epic);
        actualList = taskManager.getEpics();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void getSubTasksMethodWithEmptySubtasks() {
        ArrayList<SubTask> expectedList = new ArrayList<>();
        List<SubTask> actualList;

        actualList = taskManager.getSubTasks();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void getSubTasksMethodWithSubTasks() {
        ArrayList<SubTask> expectedList = new ArrayList<>() {{
            this.add(subTask);
            this.add(subTask);
            this.add(subTask);
        }};
        List<SubTask> actualList;

        epic.setId(1);
        taskManager.newEpic(epic);
        subTask.setEpicId(1);
        taskManager.newSubTask(subTask);
        taskManager.newSubTask(subTask);
        taskManager.newSubTask(subTask);
        actualList = taskManager.getSubTasks();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void deleteAllMethodWithAllTypeOfTasks() {
        ArrayList<Task> expectedList = new ArrayList<>() {
        };
        ArrayList<Task> actualList;

        taskManager.newTask(task);
        epic.setId(2);
        taskManager.newEpic(epic);
        subTask.setEpicId(2);
        taskManager.newSubTask(subTask);
        taskManager.deleteAll();
        actualList = taskManager.getAllTasks();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void deleteAllMethodWithEmptyTasks() {
        ArrayList<Task> expectedList = new ArrayList<>() {
        };
        ArrayList<Task> actualList;

        taskManager.deleteAll();
        actualList = taskManager.getAllTasks();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void deleteAllTasksMethodWithTasks() {
        ArrayList<Task> expectedList = new ArrayList<>() {
        };
        List<Task> actualList;

        taskManager.newTask(task);
        taskManager.newTask(task);
        taskManager.newTask(task);
        taskManager.deleteAllTasks();
        actualList = taskManager.getTasks();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void deleteAllTasksMethodWithEmptyTasks() {
        ArrayList<Task> expectedList = new ArrayList<>() {
        };
        List<Task> actualList;

        taskManager.deleteAllTasks();
        actualList = taskManager.getTasks();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void deleteAllEpicsMethodWithEpics() {
        ArrayList<Epic> expectedList = new ArrayList<>() {
        };
        List<Epic> actualList;

        taskManager.newTask(epic);
        taskManager.newTask(epic);
        taskManager.newTask(epic);
        taskManager.deleteAllEpics();
        actualList = taskManager.getEpics();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void deleteAllEpicsMethodWithEmptyEpics() {
        ArrayList<Epic> expectedList = new ArrayList<>() {
        };
        List<Epic> actualList;

        taskManager.deleteAllEpics();
        actualList = taskManager.getEpics();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void deleteAllSubTasksMethodWithSubTasks() {
        ArrayList<SubTask> expectedList = new ArrayList<>() {
        };
        List<SubTask> actualList;

        epic.setId(1);
        taskManager.newEpic(epic);
        subTask.setEpicId(1);
        taskManager.newTask(subTask);
        taskManager.newTask(subTask);
        taskManager.newTask(subTask);
        taskManager.deleteAllTasks();
        actualList = taskManager.getSubTasks();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void deleteAllSubTasksMethodWithEmptySubTasks() {
        ArrayList<SubTask> expectedList = new ArrayList<>() {
        };
        List<SubTask> actualList;

        taskManager.deleteAllSubTasks();
        actualList = taskManager.getSubTasks();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void getTaskMethodShouldReturnCorrectTask() {
        Task expectedTask = task;
        Task actualTask;

        taskManager.newTask(task);
        actualTask = taskManager.getTask(1);

        assertEquals(expectedTask, actualTask);
    }

    @Test
    public void getTaskMethodShouldReturnNullIfEmpty() {
        Task actualTask;

        actualTask = taskManager.getTask(1);

        assertNull(actualTask);
    }

    @Test
    public void getEpicMethodShouldReturnCorrectEpic() {
        Task expectedEpic = epic;
        Task actualEpic;

        epic.setId(1);
        taskManager.newEpic(epic);
        actualEpic = taskManager.getEpic(1);

        assertEquals(expectedEpic, actualEpic);
    }

    @Test
    public void getEpicMethodShouldReturnNullIfEmpty() {
        Task actualEpic;

        actualEpic = taskManager.getEpic(1);

        assertNull(actualEpic);
    }

    @Test
    public void getSubTaskShouldReturnCorrectSubTask() {
        SubTask expectedSubTask = subTask;
        SubTask actualSubTask;

        epic.setId(1);
        taskManager.newEpic(epic);
        subTask.setEpicId(1);
        taskManager.newSubTask(subTask);
        actualSubTask = taskManager.getSubTask(2);

        assertEquals(expectedSubTask, actualSubTask);
    }

    @Test
    public void getSubTaskShouldReturnNullIfEmpty() {
        SubTask actualSubTask;

        actualSubTask = taskManager.getSubTask(2);

        assertNull(actualSubTask);
    }

    @Test
    public void getSubTaskShouldReturnNullIfNoEpic() {
        SubTask actualSubTask;

        taskManager.newSubTask(subTask);
        actualSubTask = taskManager.getSubTask(2);

        assertNull(actualSubTask);
    }

    @Test
    public void taskManagerShouldReturnTaskAfterNewTaskMethod() {
        Task actualTask;
        int taskId;

        taskId = taskManager.newTask(task);
        actualTask = taskManager.getTask(taskId);

        assertEquals(1, taskId);
        assertEquals(task, actualTask);
    }

    @Test
    public void taskManagerShouldReturnEpicAfterNewEpicMethod() {
        Epic actualEpic;
        int epicId;

        epicId = taskManager.newEpic(epic);
        actualEpic = taskManager.getEpic(epicId);

        assertEquals(1, epicId);
        assertEquals(epic, actualEpic);
    }

    @Test
    public void taskManagerShouldReturnSubTaskAfterNewSubTaskMethod() {
        SubTask actualSubTask;
        int subTaskId;

        epic.setId(1);
        taskManager.newEpic(epic);
        subTask.setEpicId(1);
        subTaskId = taskManager.newSubTask(subTask);
        actualSubTask = taskManager.getSubTask(subTaskId);

        assertEquals(2, subTaskId);
        assertEquals(subTask, actualSubTask);
    }

    @Test
    public void shouldUpdateTaskStatusToDone() {
        Status expectedStatus = Status.DONE;
        Status actualStatus;
        int taskId;
        Task newTask = new Task("task", "test", Status.DONE);

        taskId = taskManager.newTask(task);
        newTask.setId(task.getId());
        taskManager.updateTask(newTask);
        actualStatus = taskManager.getTask(taskId).getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void shouldUpdateEpicStatusToDone() {
        Status expectedStatus = Status.DONE;
        Status actualStatus;
        int epicId;
        Epic newEpic = new Epic("epic", "test", Status.DONE);

        epicId = taskManager.newEpic(epic);
        newEpic.setId(epic.getId());
        taskManager.updateEpic(newEpic);
        actualStatus = taskManager.getEpic(epicId).getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void shouldUpdateSubTaskStatusToDone() {
        Status expectedStatus = Status.DONE;
        Status actualStatus;
        int subTaskId;
        SubTask newSubTask = new SubTask("SubTask", "test", Status.DONE, epic);

        taskManager.newEpic(epic);
        subTask.setEpicId(1);
        newSubTask.setEpicId(1);
        subTaskId = taskManager.newSubTask(subTask);
        newSubTask.setId(taskManager.getSubTask(2).getId());
        taskManager.updateSubTask(newSubTask);
        actualStatus = taskManager.getSubTask(subTaskId).getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void deleteTaskByIdMethodShouldDeleteTaskFromTaskManager() {
        List<Task> taskList;

        taskManager.newTask(task);
        taskManager.newTask(task);
        taskManager.newTask(task);
        taskManager.deleteTaskById(2);
        taskList = taskManager.getTasks();

        assertEquals(2, taskList.size());
    }

    @Test
    public void deleteEpicByIdMethodShouldDeleteEpicFromTaskManager() {
        List<Epic> epicList;

        taskManager.newEpic(epic);
        taskManager.newEpic(epic);
        taskManager.newEpic(epic);
        taskManager.deleteEpicById(1);
        taskManager.deleteEpicById(2);
        epicList = taskManager.getEpics();

        assertEquals(1, epicList.size());
    }

    @Test
    public void deleteSubTaskByIdMethodShouldDeleteSubTaskFromTaskManager() {
        List<SubTask> subTaskList;

        epic.setId(0);
        taskManager.newEpic(epic);
        subTask.setEpicId(epic.getId());
        taskManager.newSubTask(subTask);
        taskManager.newSubTask(subTask);
        taskManager.newSubTask(subTask);
        taskManager.deleteSubTaskById(3);
        subTaskList = taskManager.getSubTasks();

        assertEquals(2, subTaskList.size());
    }

    @Test
    public void getHistoryMethodShouldReturnListOfTaskEpicAndSubTask() {
        List<Task> expectedList = List.of(
                task,
                epic,
                subTask
        );
        List<Task> actualList;

        taskManager.newTask(task);
        taskManager.newEpic(epic);
        subTask.setEpicId(epic.getId());
        taskManager.newSubTask(subTask);
        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubTask(subTask.getId());
        actualList = taskManager.getHistory();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void shouldReturnTwoHoursDurationFromTask() {
        Duration expectedDuration = Duration.ofHours(2);
        Duration actualDuration;

        taskManager.newTask(task);
        actualDuration = taskManager.getTask(1).getDuration();

        assertEquals(expectedDuration, actualDuration);
    }


    @Test
    public void shouldReturnNullDurationAndStartTimeAndEndTimeForEpicWithoutSubtasks() {
        Duration actualDuration;
        LocalDateTime actualStartTime;
        LocalDateTime actualEndTime;

        taskManager.newEpic(epic);
        actualDuration = epic.getDuration();
        actualStartTime = epic.getStartTime();
        actualEndTime = epic.getEndTime();

        assertNull(actualDuration);
        assertNull(actualStartTime);
        assertNull(actualEndTime);
    }

    @Test
    public void shouldReturnDurationOfThreeAndStartTimeForEpicWithOneSubTask() {
        Duration expectedDuration = Duration.ofHours(3);
        Duration actualDuration;
        LocalDateTime expectedStartTime = subTask.getStartTime();
        LocalDateTime actualStartTime;
        LocalDateTime expectedEndTime = subTask.getEndTime();
        LocalDateTime actualEndTime;

        epic.setId(1);
        taskManager.newEpic(epic);
        subTask.setEpicId(1);
        taskManager.newSubTask(subTask);

        actualDuration = epic.getDuration();
        actualStartTime = epic.getStartTime();
        actualEndTime = epic.getEndTime();

        assertEquals(expectedDuration, actualDuration);
        assertEquals(expectedStartTime, actualStartTime);
        assertEquals(expectedEndTime, actualEndTime);
    }

    @Test
    public void shouldReturnStartTimeOfFirstSubTaskForEpicWithTwoSubTasks() {
        SubTask firstSubTask = subTask;
        SubTask secondSubTask = new SubTask("SubTask 2", "test", Status.NEW, epic, Duration.ofHours(3),
                LocalDateTime.from(task.getStartTime().plusHours(6)));
        LocalDateTime expectedStartTime = firstSubTask.getStartTime();
        LocalDateTime actualStartTime;

        epic.setId(1);
        taskManager.newEpic(epic);
        subTask.setEpicId(1);
        secondSubTask.setEpicId(1);
        taskManager.newSubTask(subTask);
        taskManager.newSubTask(secondSubTask);
        actualStartTime = epic.getStartTime();

        assertEquals(expectedStartTime, actualStartTime);
    }

    @Test
    public void shouldReturnEndTimeOfSecondSubTaskForEpicWithTwoSubTasks() {
        SubTask secondSubTask = new SubTask("SubTask 2", "test", Status.NEW, epic, Duration.ofHours(3),
                LocalDateTime.from(task.getStartTime().plusHours(6)));
        LocalDateTime expectedEndTime = secondSubTask.getEndTime();
        LocalDateTime actualEndTime;

        epic.setId(1);
        taskManager.newEpic(epic);
        subTask.setEpicId(1);
        secondSubTask.setEpicId(1);
        taskManager.newSubTask(subTask);
        taskManager.newSubTask(secondSubTask);
        actualEndTime = epic.getEndTime();

        assertEquals(expectedEndTime, actualEndTime);
    }

    @Test
    public void shouldReturnEndTimeOfFirstSubTaskAfterDeleteOfSecondFromEpic() {
        SubTask firstSubTask = subTask;
        SubTask secondSubTask = new SubTask("SubTask 2", "test", Status.NEW, epic, Duration.ofHours(3),
                LocalDateTime.from(task.getStartTime().plusHours(6)));
        LocalDateTime expectedEndTime = firstSubTask.getEndTime();
        LocalDateTime actualEndTime;

        epic.setId(1);
        taskManager.newEpic(epic);
        subTask.setEpicId(1);
        secondSubTask.setEpicId(1);
        taskManager.newSubTask(subTask);
        taskManager.newSubTask(secondSubTask);
        taskManager.deleteSubTaskById(3);
        actualEndTime = epic.getEndTime();

        assertEquals(expectedEndTime, actualEndTime);
    }

    @Test
    public void shouldReturnSumOfAllSubTasksDurationFromEpic() {
        SubTask secondSubTask = new SubTask("SubTask 2", "test", Status.NEW, epic, Duration.ofHours(6),
                LocalDateTime.from(task.getStartTime().plusHours(6)));
        Duration expectedDuration = Duration.ofHours(9);
        Duration actualDuration;

        epic.setId(1);
        taskManager.newEpic(epic);
        subTask.setEpicId(1);
        secondSubTask.setEpicId(1);
        taskManager.newSubTask(subTask);
        taskManager.newSubTask(secondSubTask);
        actualDuration = epic.getDuration();

        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void shouldReturnCorrectEndTimeForSubTask() {
        LocalDateTime expectedEndTime = LocalDateTime.from(actualDateTime.plusHours(6));
        LocalDateTime actualEndTime;

        epic.setId(1);
        taskManager.newEpic(epic);
        subTask.setEpicId(1);
        taskManager.newSubTask(subTask);
        actualEndTime = taskManager.getSubTask(2).getEndTime();

        assertEquals(expectedEndTime, actualEndTime);
    }

    @Test
    public void shouldReturnEmptySetForEmptyPrioritizedSet() {
        Set<Task> expectedSet = new TreeSet<>();
        Set<Task> actualSet;

        actualSet = taskManager.getPrioritizedTask();

        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void shouldReturnActualSetForNotEmptyPrioritizedSet() {
        epic.setId(2);
        subTask.setEpicId(2);
        Set<Task> expectedSet = Set.of(
                task,
                subTask,
                epic
        );
        Set<Task> actualSet;

        taskManager.newTask(task);
        taskManager.newEpic(epic);
        taskManager.newSubTask(subTask);
        actualSet = taskManager.getPrioritizedTask();

        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void shouldReturnBothTasksWithoutTimeCollisionsFromTaskManager() {
        Task task1 = new Task("task1", "test", Status.NEW, Duration.ofHours(2), actualDateTime);
        Task task2 = new Task("task2", "test", Status.NEW, Duration.ofHours(2), actualDateTime.plusHours(3));
        List<Task> expectedList = List.of(
                task1,
                task2
        );
        Set<Task> expectedSet = Set.of(
                task1,
                task2
        );
        List<Task> actualList;
        Set<Task> actualSet;

        taskManager.newTask(task1);
        taskManager.newTask(task2);
        actualList = taskManager.getAllTasks();
        actualSet = taskManager.getPrioritizedTask();

        assertEquals(expectedList, actualList);
        assertEquals(expectedSet, actualSet);

    }

    @Test
    public void shouldReturnOneTaskFromTwoWithTimeCollisionsFromTaskManager() {
        Task task1 = new Task("task1", "test", Status.NEW, Duration.ofHours(2), actualDateTime);
        Task task2 = new Task("task2", "test", Status.NEW, Duration.ofHours(2), actualDateTime.plusHours(1));
        List<Task> expectedList = List.of(
                task1
        );
        Set<Task> expectedSet = Set.of(
                task1
        );
        List<Task> actualList;
        Set<Task> actualSet;

        taskManager.newTask(task1);
        taskManager.newTask(task2);
        actualList = taskManager.getAllTasks();
        actualSet = taskManager.getPrioritizedTask();

        assertEquals(expectedList, actualList);
        assertEquals(expectedSet, actualSet);

    }
}
