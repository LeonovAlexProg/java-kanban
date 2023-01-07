package ru.yandex.practicum.manager;

public class Manager {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBacked() {
        //не понимаю как указать путь к файлу в виде одного имени, выдаёт FileNotFoundException
        // return new FileBackedTasksManager("TaskFile.csv");
        return new FileBackedTasksManager("src/ru/yandex/practicum/resources/TaskFile.csv");
    }
}
