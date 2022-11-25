package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager{
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int id;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.id = 0;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();

        allTasks.addAll(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subTasks.values());
        return allTasks;
    }

    public ArrayList<Task> getTasks() {

        return new ArrayList<>(tasks.values());

    }

    public ArrayList<Epic> getEpics() {

        return new ArrayList<>(epics.values());

    }

    public ArrayList<SubTask> getSubTasks() {

        return new ArrayList<>(subTasks.values());

    }

    @Override
    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    @Override
    public SubTask getSubTasksById(Integer id) {
        return subTasks.get(id);
    }

    @Override
    public int newTask(Task task) {
        int id = this.id++;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int newEpic(Epic epic) {
        int id = this.id++;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public int newSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            int id = this.id++;
            subTask.setId(id);
            subTasks.put(id, subTask);
            updateEpicSubTasks(epics.get(subTask.getEpicId()), subTask);
            return id;
        }
        return -1;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId()))
            tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId()))
            epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            updateEpicStatus(epics.get(subTask.getEpicId()));
        }
    }

    private void updateEpicSubTasks(Epic epic, SubTask subTask) {
        if (epics.containsKey(epic.getId()) && subTasks.containsKey(subTask.getId())) {
            ArrayList<Integer> subTasks = epic.getSubTasks();

            if (!subTasks.contains(subTask.getId()))
                subTasks.add(subTask.getId());
        }
    }

    @Override
    public void deleteTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    @Override
    public void deleteEpicById(Integer id) {
        if (epics.containsKey(id)) {
            deleteAllSubTasks(epics.get(id));
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        if (subTasks.containsKey(id)) {
            deleteSubTask(id);
            subTasks.remove(id);
        }
    }

    private void deleteAllSubTasks(Epic epic) {
        epic.getSubTasks().clear();
    }

    private void deleteSubTask(Integer subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);

        epics.get(subTask.getEpicId()).getSubTasks().remove(subTaskId);
        subTasks.remove(subTaskId);
    }

    private ArrayList<SubTask> getEpicSubTasks(Epic epic) {
        ArrayList<SubTask> subTasks = new ArrayList<>();

        for (Integer subTaskId : epic.getSubTasks()) {
            subTasks.add(this.subTasks.get(subTaskId));
        }
        return subTasks;
    }

    private void updateEpicStatus(Epic epic) {
        boolean isNew = true;
        boolean isDone = true;

        for (Integer subTask : epic.getSubTasks()) {
            String subTaskStatus = subTasks.get(subTask).getStatus();

            if (!subTaskStatus.equals(Status.NEW.toString()))
                isNew = false;

            if (!subTaskStatus.equals(Status.DONE.toString()))
                isDone = false;
        }
        if (isNew) {
            epic.setStatus(Status.NEW.toString());
        } else if (isDone) {
            epic.setStatus(Status.DONE.toString());
        } else {
            epic.setStatus(Status.IN_PROGRESS.toString());
        }
    }

}
