package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager{
    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, SubTask> subTasks;
    protected int id;
    protected Set<Task> prioritizedTasks;
    HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.id = 1;
        historyManager = Manager.getDefaultHistory();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
                Comparator.nullsFirst(Comparator.naturalOrder())).thenComparing(Task::getId));
    }

    protected boolean checkIntersections(Task task) {
        if (task.getStartTime() != null & task.getDuration() != null) {
            LocalDateTime taskStartTime = task.getStartTime();
            LocalDateTime taskEndTime = task.getEndTime();

            for (Task currentTask: getPrioritizedTask()) {
                if (currentTask.getStartTime() == null && currentTask.getDuration() == null) continue;

                LocalDateTime currentTaskStartTime = currentTask.getStartTime();
                LocalDateTime currentTaskEndTIme = currentTask.getEndTime();

                if (taskStartTime.isBefore(currentTaskStartTime) && taskEndTime.isAfter(currentTaskStartTime))
                    return false;
                if (taskStartTime.isBefore(currentTaskEndTIme) && taskEndTime.isAfter(currentTaskEndTIme))
                    return false;
                if (taskStartTime.isAfter(currentTaskStartTime) && taskEndTime.isBefore(currentTaskEndTIme))
                    return false;
                return true;
            }
        }
        return true;
    }

    @Override
    public List<Task> getPrioritizedTask() {
        return new ArrayList<>(prioritizedTasks);
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
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
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public int newTask(Task task) {
        if (checkIntersections(task)) {
            int id = this.id++;
            task.setId(id);
            tasks.put(id, task);
            prioritizedTasks.add(task);
            return id;
        }
        return -1;
    }

    @Override
    public int newEpic(Epic epic) {
        if (checkIntersections(epic)) {
            int id = this.id++;
            epic.setId(id);
            calculateEpicTime(epic);
            epics.put(id, epic);
            return id;
        }
        return -1;
    }

    @Override
    public int newSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId()) && checkIntersections(subTask)) {
            int id = this.id++;
            subTask.setId(id);
            subTasks.put(id, subTask);
            prioritizedTasks.add(subTask);
            updateEpicSubTasks(epics.get(subTask.getEpicId()), subTask);
            updateEpicStatus(epics.get(subTask.getEpicId()));
            calculateEpicTime(epics.get(subTask.getEpicId()));
            return id;
        }
        return -1;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId()) && checkIntersections(task))
            tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId()) && checkIntersections(epic)) {
            calculateEpicTime(epic);
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId()) && checkIntersections(subTask)) {
            subTasks.put(subTask.getId(), subTask);
            updateEpicStatus(epics.get(subTask.getEpicId()));
            calculateEpicTime(epics.get(subTask.getEpicId()));
        }
    }
    protected void updateEpicSubTasks(Epic epic, SubTask subTask) {
        if (epics.containsKey(epic.getId()) && subTasks.containsKey(subTask.getId())) {
            ArrayList<Integer> subTasks = epic.getSubTasks();

            if (!subTasks.contains(subTask.getId()))
                subTasks.add(subTask.getId());
        }
    }

    @Override
    public void deleteTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
        }
    }

    @Override
    public void deleteEpicById(Integer id) {
        if (epics.containsKey(id)) {
            deleteEpicSubTasks(epics.get(id));
            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        if (subTasks.containsKey(id)) {
            historyManager.remove(id);
            SubTask subTask = subTasks.get(id);

            epics.get(subTask.getEpicId()).getSubTasks().remove(id);
            prioritizedTasks.remove(subTask);
            subTasks.remove(id);
            updateEpicStatus(epics.get(subTask.getEpicId()));
            calculateEpicTime(epics.get(subTask.getEpicId()));
        }
    }

    private void deleteEpicSubTasks(Epic epic) {
        for (Integer subTask : epic.getSubTasks()) {
            historyManager.remove(subTask);
            subTasks.remove(subTask);
        }

        calculateEpicTime(epic);
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

    protected void updateEpicStatus(Epic epic) {
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

    protected void calculateEpicTime(Epic epic) {
        if (!epic.getSubTasks().isEmpty()) {
            ArrayList<SubTask> epicSubTasks = getEpicSubTasks(epic.getId());
            
            LocalDateTime earliestDateTime = epic.getStartTime();
            LocalDateTime latestDateTime = epic.getEndTime();
            Duration allDuration = Duration.ZERO;

            if (!epicSubTasks.isEmpty()) {
                earliestDateTime = epicSubTasks.get(0).getStartTime();
                latestDateTime = epicSubTasks.get(0).getEndTime();
            } else {
                earliestDateTime = null;
                latestDateTime = null;
                epic.setStartTime(null);
                epic.setDuration(null);
                epic.setEndTime(null);
            }

            for (SubTask subTask : epicSubTasks) {
                if (subTask.getStartTime() != null) {
                    if (subTask.getStartTime().isBefore(earliestDateTime)) {
                        earliestDateTime = subTask.getStartTime();
                    }
                    if (subTask.getEndTime().isAfter(latestDateTime)) {
                        latestDateTime = subTask.getEndTime();
                    }
                }

                if (subTask.getDuration() != null) {
                    allDuration = allDuration.plus(subTask.getDuration());
                }
            }

            if (earliestDateTime != null && !earliestDateTime.isEqual(LocalDateTime.MAX)) {
                epic.setStartTime(earliestDateTime);
            }
            if (latestDateTime != null && !latestDateTime.isEqual(LocalDateTime.MAX)) {
                epic.setEndTime(latestDateTime);
            }
            if (allDuration != null) {
                epic.setDuration(allDuration);
            }
        }
    }
}
