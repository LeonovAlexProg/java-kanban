package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int id;
    HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.id = 0;
        historyManager = Manager.getDefaultHistory();
    }

    public List<Task> getHistory() {
        List<Task> taskHistory = new ArrayList<>();
        List<Task> allTasks = getAllTasks();

        for (Integer id : historyManager.getHistory()) {
            taskHistory.add(allTasks.get(id));
        }

        return taskHistory;
    }


    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();

        allTasks.addAll(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subTasks.values());
        return allTasks;
    }

    @Override
    public ArrayList<Task> getTasks() {

        return new ArrayList<>(tasks.values());

    }

    @Override
    public ArrayList<Epic> getEpics() {

        return new ArrayList<>(epics.values());

    }

    @Override
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
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();

        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public Task getTask(Integer id) {
        historyManager.add(id);
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(Integer id) {
        historyManager.add(id);
        return epics.get(id);
    }

    @Override
    public SubTask getSubTask(Integer id) {
        historyManager.add(id);
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
            updateEpicStatus(epics.get(subTask.getEpicId()));
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

    //данный метод не обновляет статус, он вспомогательный и вызывается внутри newSubTask() что бы добавить
    //айди нового сабтаска в список всех сабтасков конкретного эпика
    private void updateEpicSubTasks(Epic epic, SubTask subTask) {
        if (epics.containsKey(epic.getId()) && subTasks.containsKey(subTask.getId())) {
            ArrayList<Integer> subTasks = epic.getSubTasks();

            if (!subTasks.contains(subTask.getId()))
                subTasks.add(subTask.getId());
        }
    }

    @Override
    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(Integer id) {
        if (epics.containsKey(id)) {
            deleteEpicSubTasks(epics.get(id));
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        if (subTasks.containsKey(id)) {
            SubTask subTask = subTasks.get(id);

            epics.get(subTask.getEpicId()).getSubTasks().remove(id);
            subTasks.remove(id);
            updateEpicStatus(epics.get(subTask.getEpicId()));
        }
    }

    private void deleteEpicSubTasks(Epic epic) {
        for (Integer subTask : epic.getSubTasks()) {
            subTasks.remove(subTask);
        }

        epic.getSubTasks().clear();
    }

    @Override
    public ArrayList<SubTask> getEpicSubTasks(Integer id) {
        ArrayList<SubTask> subTasks = new ArrayList<>();

        for (Integer subTaskId : epics.get(id).getSubTasks()) {
            subTasks.add(this.subTasks.get(subTaskId));
        }
        return subTasks;
    }

    private void updateEpicStatus(Epic epic) {
        boolean isNew = true;
        boolean isDone = true;

        for (Integer subTask : epic.getSubTasks()) {
            Status subTaskStatus = subTasks.get(subTask).getStatus();

            if (!subTaskStatus.equals(Status.NEW))
                isNew = false;

            if (!subTaskStatus.equals(Status.DONE))
                isDone = false;
        }
        if (isNew) {
            epic.setStatus(Status.NEW);
        } else if (isDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

}
