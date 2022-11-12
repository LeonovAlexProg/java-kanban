import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int taskId;
    private int epicId;
    private int subTaskId;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.taskId = 0;
        this.epicId = 0;
        this.subTaskId = 0;
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
        for (int i = 0; i < taskId; i++) {
            tasks.remove(i);
        }
        for (int i = 0; i < epicId; i++) {
            epics.remove(i);
        }
        for (int i = 0; i < subTaskId; i++) {
            subTasks.remove(i);
        }
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

    public void newTask(Task task) {
        if (tasks.size() != 0) {
            for (int i = 0; i <= taskId; i++) {
                if (tasks.containsKey(i)) {
                    task.setId(taskId);
                    tasks.put(taskId, task);
                    taskId++;
                } else {
                    task.setId(i);
                    tasks.put(i, task);
                }
                break;
            }
        } else {
            task.setId(taskId);
            tasks.put(taskId, task);
            taskId++;
        }
    }

    public void newEpic(Epic epic) {
        epic.setId(epicId);
        epics.put(epicId,epic);
        epicId++;
    }

    public void newSubTask(SubTask subTask) {
        subTask.setId(subTaskId);
        updateEpic(subTask.getEpicId(), subTask.getId());
        subTasks.put(subTaskId,subTask);
        subTaskId++;
    }

    public void updateTask(Integer taskId, Task task) {
        tasks.put(taskId, task);
    }

    public void updateEpic(Integer epicId, Integer subTaskId) {
        ArrayList<Integer> subTasks = epics.get(epicId).getSubTasks();

        if (!subTasks.contains(subTaskId))
            subTasks.add(subTaskId);
    }

    public void updateSubTask(Integer subTaskId, SubTask subTask) {
        subTasks.put(subTaskId, subTask);
        updateEpicStatus(epics.get(subTask.getEpicId()));
    }

    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }

    public void deleteEpicById(Integer id) {
        deleteAllSubTasks(epics.get(id));
        epics.remove(id);
    }

    public void deleteSubTaskById(Integer id) {
        deleteSubTask(id);
        subTasks.remove(id);
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
        for (Integer subTask : epic.getSubTasks()) {
            String subTaskStatus = subTasks.get(subTask).getStatus();

            if (subTaskStatus.equals("IN_PROGRESS"))
                epic.setStatus("IN_PROGRESS");

            if (!subTaskStatus.equals("DONE"))
                isDone = false;
        }
        if (isDone)
            epic.setStatus("DONE");
    }
}
