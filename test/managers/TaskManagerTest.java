package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;

    public void initManager(T taskManager) {
        this.taskManager = taskManager;
    }

    @BeforeEach
    void setUpTasks() {
        task = new Task("Task 1", "test", Status.NEW);
        epic = new Epic("Epic", "test", Status.NEW);
        epic.setId(-1);
        subTask = new SubTask("SubTask 1", "test", Status.NEW, epic);
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
}
