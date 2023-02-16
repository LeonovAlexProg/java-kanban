package ru.yandex.practicum.manager;

import java.nio.file.Paths;


/*
Внёс все изменения в проект кроме редактирования данного класса. Попытка избавиться от getFileBacked и
переход на использования getDefault полностью ломает проект, вносит кучу багов отлов которых оказался мне непосилен.
 */
public class Manager {
    public static HttpTaskManager getDefault() {
        return new HttpTaskManager("localhost:8081/");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
