package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<Task> getAllTasks();
    void deleteAll();

    Task getTask(Integer id);
    Epic getEpic(Integer id);
    SubTask getSubTask(Integer id);

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
