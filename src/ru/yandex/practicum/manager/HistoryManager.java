package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(int id);

    List<Integer> getHistory();
}
