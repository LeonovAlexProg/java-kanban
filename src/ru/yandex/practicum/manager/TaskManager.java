package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<Task> getAllTasks();
    List<Task> getTasks();
    List<Epic> getEpics();
    List<SubTask> getSubTasks();

    void deleteAll();
    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubTasks();

    Task getTask(int id);
    Epic getEpic(int id);
    SubTask getSubTask(int id);

    ArrayList<SubTask> getEpicSubTasks(Integer id);

    int newTask(Task task);
    int newEpic(Epic epic);
    int newSubTask(SubTask subTask);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubTask(SubTask subTask);

    void deleteTaskById(Integer id);
    void deleteEpicById(Integer id);
    void deleteSubTaskById(Integer id);

    List<Task> getHistory();
}
