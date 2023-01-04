package ru.yandex.practicum.manager;

public class Manager {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBacked() {
        return new FileBackedTasksManager("src/ru/yandex/practicum/resources/TaskFile.csv");
    }
}
