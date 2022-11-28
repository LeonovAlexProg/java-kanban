package ru.yandex.practicum.manager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Integer> viewHistory = new ArrayList<>();

    @Override
    public void add(int id) {
        if (viewHistory.size() >= 10)
            viewHistory.remove(0);
        viewHistory.add(id);

    }

    @Override
    public List<Integer> getHistory() {
        return viewHistory;
    }
}
