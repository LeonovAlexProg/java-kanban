package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> viewStory = new ArrayList<>();

    public void add(Task task) {
        if (task != null) {
            if (viewStory.size() >= 10)
                viewStory.remove(0);
            viewStory.add(task);
        }
    }

    public List<Task> getHistory() {
        return viewStory;
    }
}
