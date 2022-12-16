package ru.yandex.practicum;

import ru.yandex.practicum.manager.*;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Manager.getDefault();

        Task firstTask = new Task("Заняться спортом", "Совершить утреннюю пробежку", Status.NEW);
        Task secondTask = new Task("Пообедать", "Приготовить обед", Status.NEW);

        int task1 = inMemoryTaskManager.newTask(firstTask);
        int task2 = inMemoryTaskManager.newTask(secondTask);

        Epic firstEpic = new Epic("Сходить в магазин", "Закупиться продуктами по списку", Status.NEW);
        int epic1 = inMemoryTaskManager.newEpic(firstEpic);

        SubTask firstEpicSubTask = new SubTask("Подготовить список",
                "подготовить список того что нужно купить",
                Status.NEW, firstEpic);
        SubTask firstEpicSubTask2 = new SubTask("Совершить покупки", "собрать корзину и оплатить товары",
                Status.NEW, firstEpic);
        SubTask firstEpicSubTask3 = new SubTask("Проектная деятельность",
                "выполнить тз для Яндекс Практикума", Status.NEW, firstEpic);
        int subTask1 = inMemoryTaskManager.newSubTask(firstEpicSubTask);
        int subTask2 = inMemoryTaskManager.newSubTask(firstEpicSubTask2);
        int subTask3 = inMemoryTaskManager.newSubTask(firstEpicSubTask3);

        Epic secondEpic = new Epic("Учиться", "Выполнить задания по учёбе", Status.NEW);
        int epic2 = inMemoryTaskManager.newEpic(secondEpic);

        inMemoryTaskManager.getTask(task1);
        inMemoryTaskManager.getEpic(epic1);
        inMemoryTaskManager.getSubTask(subTask1);
        inMemoryTaskManager.getSubTask(subTask2);
        inMemoryTaskManager.getSubTask(subTask3);
        inMemoryTaskManager.getSubTask(subTask1);
        inMemoryTaskManager.getTask(task2);
        inMemoryTaskManager.getTask(task1);

        System.out.println(inMemoryTaskManager.getHistory() + System.lineSeparator());

        inMemoryTaskManager.deleteEpicById(epic1);

        System.out.println(inMemoryTaskManager.getHistory() + System.lineSeparator());

        inMemoryTaskManager.getEpic(epic2);
        inMemoryTaskManager.getEpic(epic2);
        inMemoryTaskManager.getTask(task1);
        inMemoryTaskManager.getTask(task2);
        inMemoryTaskManager.getTask(task1);
        inMemoryTaskManager.getEpic(epic2);

        System.out.println(inMemoryTaskManager.getHistory() + System.lineSeparator());
    }
}
