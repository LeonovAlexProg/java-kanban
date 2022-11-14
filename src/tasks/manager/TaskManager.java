package tasks.manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int id;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.id = 0;
    }

    public ArrayList<Task> getAllTasks() {

        ArrayList<Task> allTasks = new ArrayList<>(tasks.values());

        for (Epic epic : epics.values()) {
            allTasks.add(epic);
            allTasks.addAll(getEpicSubTasks(epic));
        }
        return allTasks;
    }

    public ArrayList<Task> getTasks() {

        return new ArrayList<>(this.tasks.values());

    }

    public ArrayList<Epic> getEpics() {

        return new ArrayList<>(this.epics.values());

    }

    public ArrayList<SubTask> getSubTasks() {

        return new ArrayList<>(this.subTasks.values());

    }

    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    public SubTask getSubTasksById(Integer id) {
        return subTasks.get(id);
    }

    public Integer newTask(Task task) {
        int id = this.id++;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public Integer newEpic(Epic epic) {
        int id = this.id++;
        epic.setId(id);
        epics.put(id,epic);
        return id;
    }

    public Integer newSubTask(SubTask subTask) {
        int id = this.id++;
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(id);
            subTasks.put(id, subTask);
            /* в ревью мне пометили что *Не проверяется статус Epic, но при добавлении subTask он может тоже поменяться*
            Наверное я путаю, но Epic должен менять свой статус на IN_PROGRESS только когда первый из новых сабтасков,
            становится IN_PROGRESS, а эта логика у меня реализована в методе updateSubTask();
             */
            updateEpicSubTasks(epics.get(subTask.getEpicId()), subTask);
        }
        return id;
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId()))
            tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId()))
            epics.put(epic.getId(), epic);
    }

    private void updateEpicSubTasks(Epic epic, SubTask subTask) {
        if (epics.containsKey(epic.getId()) && subTasks.containsKey(subTask.getId())) {
            ArrayList<Integer> subTasks = epic.getSubTasks();

            if (!subTasks.contains(subTask.getId()))
                subTasks.add(subTask.getId());
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            updateEpicStatus(epics.get(subTask.getEpicId()));
        }
    }

    public void deleteTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void deleteEpicById(Integer id) {
        if (epics.containsKey(id)) {
            deleteAllSubTasks(epics.get(id));
            epics.remove(id);
        }
    }

    public void deleteSubTaskById(Integer id) {
        if (subTasks.containsKey(id)) {
            deleteSubTask(id);
            subTasks.remove(id);
        }
    }

    private void deleteAllSubTasks(Epic epic) {
        ArrayList<Integer> subTasks = epic.getSubTasks();
        for (Integer subTask : subTasks) {
            this.subTasks.remove(subTask);
        }
        epic.getSubTasks().removeAll(subTasks);
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
        boolean isDone = true;

        if (!epic.getSubTasks().isEmpty()) {
            for (Integer subTask : epic.getSubTasks()) {
                String subTaskStatus = subTasks.get(subTask).getStatus();

                if (subTaskStatus.equals("IN_PROGRESS"))
                    epic.setStatus("IN_PROGRESS");

                if (!subTaskStatus.equals("DONE"))
                    isDone = false;
            }
            /* в данном методе у меня проводится проверка статусов сабтасков внутри эпика.
            в цикле я перебираю все сабтаски, и если есть хотя бы один IN_PROGRESS, то статус эпика становится тоже
            IN_PROGRESS. Однако параллельно этому я установил флаг isDone, который будет true только если все
            сабтаски будут DONE. Честно говоря я не понимаю где у меня тут ошибку( Добавил проверку на наличие сабтасков
            в принципе.
             */
            if (isDone)
                epic.setStatus("DONE");
        }
    }
}
