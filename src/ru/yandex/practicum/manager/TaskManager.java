package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getAllTasks();
    void deleteAll();

    Task getTaskById(Integer id);
    Epic getEpicById(Integer id);
    SubTask getSubTasksById(Integer id);

    int newTask(Task task);
    int newEpic(Epic epic);
    int newSubTask(SubTask subTask);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubTask(SubTask subTask);

    void deleteTaskById(Integer id);
    void deleteEpicById(Integer id);
    void deleteSubTaskById(Integer id);
}
