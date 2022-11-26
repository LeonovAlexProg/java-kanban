package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Integer> viewStory = new ArrayList<>();

    @Override
    public void add(int id) {
        if (viewStory.size() >= 10)
            viewStory.remove(0);
        viewStory.add(id);

    }

    @Override
    public List<Integer> getHistory() {
        return viewStory;
    }
}
