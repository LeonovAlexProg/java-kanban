package ru.yandex.practicum;

import ru.yandex.practicum.manager.*;
import ru.yandex.practicum.servers.httptaskserver.HttpTaskServer;
import ru.yandex.practicum.servers.kvserver.KVServer;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer server = new HttpTaskServer();
        server.startTaskServer();
    }


//    public static void main(String[] args) {
//        TaskManager httpTaskManager =
//                new FileBackedTasksManager("src/ru/yandex/practicum/resources/TaskFile.csv");
//
//        Task firstTask = new Task("Заняться спортом", "Совершить утреннюю пробежку", Status.NEW);
//        Task secondTask = new Task("Пообедать", "Приготовить обед", Status.NEW);
//
//        int task1 = httpTaskManager.newTask(firstTask);
//        int task2 = httpTaskManager.newTask(secondTask);
//
//        Epic firstEpic = new Epic("Сходить в магазин", "Закупиться продуктами по списку", Status.NEW);
//        int epic1 = httpTaskManager.newEpic(firstEpic);
//
//        SubTask firstEpicSubTask = new SubTask("Подготовить список",
//                "подготовить список того что нужно купить",
//                Status.NEW, firstEpic.getId());
//        SubTask firstEpicSubTask2 = new SubTask("Совершить покупки", "собрать корзину и оплатить товары",
//                Status.NEW, firstEpic.getId());
//        SubTask firstEpicSubTask3 = new SubTask("Проектная деятельность",
//                "выполнить тз для Яндекс Практикума", Status.NEW, firstEpic.getId());
//        int subTask1 = httpTaskManager.newSubTask(firstEpicSubTask);
//        int subTask2 = httpTaskManager.newSubTask(firstEpicSubTask2);
//        int subTask3 = httpTaskManager.newSubTask(firstEpicSubTask3);
//
//         Epic secondEpic = new Epic("Учиться", "Выполнить задания по учёбе", Status.NEW);
//        int epic2 = httpTaskManager.newEpic(secondEpic);
//
//        httpTaskManager.getTask(task1);
//        httpTaskManager.getEpic(epic1);
//        httpTaskManager.getSubTask(subTask1);
//        httpTaskManager.getSubTask(subTask2);
//        httpTaskManager.getSubTask(subTask3);
//        httpTaskManager.getSubTask(subTask1);
//        httpTaskManager.getTask(task2);
//        httpTaskManager.getTask(task1);
//
//        System.out.println(httpTaskManager.getHistory() + System.lineSeparator());
//
//        httpTaskManager.deleteEpicById(epic1);
//
//        System.out.println(httpTaskManager.getHistory() + System.lineSeparator());
//
//        httpTaskManager.getEpic(epic2);
//        httpTaskManager.getEpic(epic2);
//        httpTaskManager.getTask(task1);
//        httpTaskManager.getTask(task2);
//        httpTaskManager.getTask(task1);
//        httpTaskManager.getEpic(epic2);
//
//        System.out.println(httpTaskManager.getHistory() + System.lineSeparator());
//    }
}
