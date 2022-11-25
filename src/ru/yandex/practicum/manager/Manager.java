package ru.yandex.practicum.manager;

public class Manager {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
