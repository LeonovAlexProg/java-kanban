package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private static Path CSV_FILE = null;

    public FileBackedTasksManager(String csvFilePath) {
        super();
        CSV_FILE = Paths.get(csvFilePath);
    }

    private void save() {
        try (FileWriter fw = new FileWriter(CSV_FILE.toFile())) {
            fw.write("id,type,name,status,description,epic\n");

            for (Task tasks : tasks.values()) {
                fw.write(tasks.toCsvString());
            }

            for (Epic epics : epics.values()) {
                fw.write(epics.toCsvString());
            }

            for (SubTask subTasks : subTasks.values()) {
                fw.write(subTasks.toCsvString());
            }
            fw.write("\n");

            if (historyManager.getHistory().size() > 0) {
                fw.write(historyToString(historyManager));
            }
        } catch (IOException exception) {
            System.out.println(exception);
        }
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder strBuilder = new StringBuilder();
        List<Task> history = manager.getHistory();

        for (Task tasks : history) {
            strBuilder.append(tasks.getId() + ",");
        }
        strBuilder.deleteCharAt(strBuilder.length() - 1);

        return strBuilder.toString();
    }

    //TODO
//    static List<Integer> historyFromString(String value) {
//
//    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public int newTask(Task task) {
        int id = this.id++;
        task.setId(id);
        tasks.put(id, task);
        save();
        return id;
    }

    @Override
    public int newEpic(Epic epic) {
        int id = this.id++;
        epic.setId(id);
        epics.put(id, epic);
        save();
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
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        save();
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        save();
        return epics.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(subTasks.get(id));
        save();
        return subTasks.get(id);
    }

    public static void main(String[] args) {
        FileBackedTasksManager tm = Manager.getFileBacked();

        Task firstTask = new Task("Заняться спортом", "Совершить утреннюю пробежку", Status.NEW);
        Task secondTask = new Task("Пообедать", "Приготовить обед", Status.NEW);

        int task1 = tm.newTask(firstTask);
        int task2 = tm.newTask(secondTask);

        Epic firstEpic = new Epic("Сходить в магазин", "Закупиться продуктами по списку", Status.NEW);
        int epic1 = tm.newEpic(firstEpic);

        SubTask firstEpicSubTask = new SubTask("Подготовить список",
                "подготовить список того что нужно купить",
                Status.NEW, firstEpic);
        SubTask firstEpicSubTask2 = new SubTask("Совершить покупки", "собрать корзину и оплатить товары",
                Status.NEW, firstEpic);
        SubTask firstEpicSubTask3 = new SubTask("Проектная деятельность",
                "выполнить тз для Яндекс Практикума", Status.NEW, firstEpic);
        int subTask1 = tm.newSubTask(firstEpicSubTask);
        int subTask2 = tm.newSubTask(firstEpicSubTask2);
        int subTask3 = tm.newSubTask(firstEpicSubTask3);

        Epic secondEpic = new Epic("Учиться", "Выполнить задания по учёбе", Status.NEW);
        int epic2 = tm.newEpic(secondEpic);

        tm.getTask(task1);
        tm.getEpic(epic1);
        tm.getSubTask(subTask1);
        tm.getSubTask(subTask2);
        tm.getSubTask(subTask3);
        tm.getSubTask(subTask1);
        tm.getTask(task2);
        tm.getTask(task1);

    }
}
