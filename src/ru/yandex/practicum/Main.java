package ru.yandex.practicum;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.manager.InMemoryTaskManager;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task firstTask = new Task("Заняться спортом", "Совершить утреннюю пробежку", "NEW");
        Task secondTask = new Task("Пообедать", "Приготовить обед", "NEW");

        int task1 = inMemoryTaskManager.newTask(firstTask);
        int task2 = inMemoryTaskManager.newTask(secondTask);

        Epic firstEpic = new Epic("Сходить в магазин", "Закупиться продуктами по списку", "NEW");
        int epic1 = inMemoryTaskManager.newEpic(firstEpic);

        SubTask firstEpicSubTask = new SubTask("Подготовить список",
                "подготовить список того что нужно купить",
                "NEW", firstEpic);
        SubTask firstEpicSubTask2 = new SubTask("Совершить покупки", "собрать корзину и оплатить товары",
                "NEW", firstEpic);
        int subTask1 = inMemoryTaskManager.newSubTask(firstEpicSubTask);
        int subTask2 = inMemoryTaskManager.newSubTask(firstEpicSubTask2);

        Epic secondEpic = new Epic("Учиться", "Выполнить задания по учёбе", "NEW");
        int epic2 = inMemoryTaskManager.newEpic(secondEpic);

        SubTask secondEpicSubTask = new SubTask("Проектная деятельность",
                "выполнить тз для Яндекс Практикума", "NEW", secondEpic);
        int subTask3 = inMemoryTaskManager.newSubTask(secondEpicSubTask);

        System.out.println(inMemoryTaskManager.getAllTasks());

        firstTask.setStatus(Status.IN_PROGRESS.toString());

        firstEpicSubTask.setStatus(Status.DONE.toString());
        firstEpicSubTask2.setStatus(Status.DONE.toString());
        secondEpicSubTask.setStatus(Status.DONE.toString());

        inMemoryTaskManager.updateTask(firstTask);
        inMemoryTaskManager.updateSubTask(firstEpicSubTask);
        inMemoryTaskManager.updateSubTask(secondEpicSubTask);

        System.out.println(inMemoryTaskManager.getAllTasks());

        inMemoryTaskManager.deleteTaskById(task1);
        inMemoryTaskManager.deleteEpicById(epic1);
        inMemoryTaskManager.deleteSubTaskById(subTask3);

        System.out.println(inMemoryTaskManager.getAllTasks());

        System.out.println(inMemoryTaskManager.getEpicById(epic2));

        inMemoryTaskManager.deleteAll();

        System.out.println(inMemoryTaskManager.getAllTasks());
    }
}
