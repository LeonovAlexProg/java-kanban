package ru.yandex.practicum.manager;

import java.nio.file.Paths;

public class Manager {
    public static TaskManager getDefault() {
        return FileBackedTasksManager.loadFromFile(Paths.get("test/resources/TaskFile.csv").toFile());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
