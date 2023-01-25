package ru.yandex.practicum.manager;

import ru.yandex.practicum.exceptions.ManagerSaveException;
import ru.yandex.practicum.tasks.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private final Path csvFile;

    public FileBackedTasksManager(String csvFilePath) {
        super();
        csvFile = Paths.get(csvFilePath);
    }

    private void save(){
        try (FileWriter fw = new FileWriter(csvFile.toFile())){
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

            if (!historyManager.getHistory().isEmpty()) {
                fw.write(historyToString(historyManager));
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка во время сохранения в файл");
        }
    }

    //Изменил сигнатуру метода, т.к. старая не соответствовала тз. Сам заметил...
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager tm = new FileBackedTasksManager(file.getPath());
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                List<String> lines = new ArrayList<>();

                while (br.ready()) {
                    lines.add(br.readLine());
                }

                for (int i = 1; true; i++) {
                    if (lines.get(i).isEmpty()) {
                        break;
                    }

                    String[] taskLine = lines.get(i).split(",");
                    int taskId = Integer.parseInt(taskLine[0]);

                    if (tm.id < taskId)
                        tm.id = taskId + 1;

                    if (taskLine[1].equals(TaskTypes.TASK.toString())) {
                        Task task = new Task(taskLine[2], taskLine[4], Status.valueOf(taskLine[3]));
                        task.setId(taskId);

                        tm.tasks.put(task.getId(), task);
                    } else if (taskLine[1].equals(TaskTypes.EPIC.toString())) {
                        Epic epic = new Epic(taskLine[2], taskLine[4], Status.valueOf(taskLine[3]));
                        epic.setId(taskId);

                        tm.epics.put(epic.getId(), epic);
                    } else {
                        Epic epic = tm.epics.get(Integer.parseInt(taskLine[5]));

                        SubTask subTask = new SubTask(taskLine[2], taskLine[4], Status.valueOf(taskLine[3]), epic);
                        subTask.setId(taskId);

                        epic.getSubTasks().add(subTask.getId());

                        tm.subTasks.put(subTask.getId(), subTask);
                    }
                }

                List<Integer> history = historyFromString(lines.get(lines.size() - 1));
                if (history != null) {
                    for (Integer id : history) {
                        if (tm.tasks.containsKey(id)) {
                            tm.historyManager.add(tm.tasks.get(id));
                        } else if (tm.epics.containsKey(id)) {
                            tm.historyManager.add(tm.epics.get(id));
                        } else {
                            tm.historyManager.add(tm.subTasks.get(id));
                        }
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (IndexOutOfBoundsException exception) {
                System.out.println("Данные отсутствуют");
            }
        } else {
            System.out.println("Загрузка с файла невозможна. Загрузочный файл отсутствует");
        }
        return tm;
    }

    private static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        List<String> taskIds = new ArrayList<>();

        for (Task task : history) {
            taskIds.add(Integer.toString(task.getId()));
        }

        return taskIds.stream().collect(Collectors.joining(","));
    }

    static List<Integer> historyFromString(String value) {
        if (!value.isEmpty()) {
            List<Integer> history = new ArrayList<>();

            for (String str : value.split(",")) {
                history.add(Integer.parseInt(str));
            }

            return history;
        }
        return null;
    }

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
        int id = super.newTask(task);
        save();
        return id;
    }

    @Override
    public int newEpic(Epic epic) {
        int id = super.newEpic(epic);
        save();
        return id;
    }

    @Override
    public int newSubTask(SubTask subTask) {
        int id = super.newSubTask(subTask);
        save();
        return id;
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
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
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
        tm.getTask(task1);
        tm.getEpic(epic1);
        tm.getSubTask(subTask1);
        tm.getSubTask(subTask2);
        tm.getSubTask(subTask3);
        tm.getSubTask(subTask1);
        tm.getTask(task2);
        tm.getTask(task1);

        FileBackedTasksManager newTm = Manager.getFileBacked();
        newTm = FileBackedTasksManager.loadFromFile(newTm.csvFile.toFile());
        int epic3 = newTm.newEpic(secondEpic);

        int subTask4 = newTm.newSubTask(firstEpicSubTask);
        int subTask5 = newTm.newSubTask(firstEpicSubTask2);
        int subTask6 = newTm.newSubTask(firstEpicSubTask3);
        System.out.println(tm.getHistory());
        System.out.println(newTm.getHistory());
    }
}
