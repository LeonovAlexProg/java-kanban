import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;
import tasks.manager.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task firstTask = new Task("Заняться спортом", "Совершить утреннюю пробежку", "NEW");
        Task secondTask = new Task("Пообедать", "Приготовить обед", "NEW");

        int task1 = taskManager.newTask(firstTask);
        int task2 = taskManager.newTask(secondTask);

        Epic firstEpic = new Epic("Сходить в магазин", "Закупиться продуктами по списку", "NEW");
        int epic1 = taskManager.newEpic(firstEpic);

        SubTask firstEpicSubTask = new SubTask("Подготовить список",
                "подготовить список того что нужно купить",
                "NEW", firstEpic);
        SubTask firstEpicSubTask2 = new SubTask("Совершить покупки", "собрать корзину и оплатить товары",
                "NEW", firstEpic);
        int subTask1 = taskManager.newSubTask(firstEpicSubTask);
        int subTask2 = taskManager.newSubTask(firstEpicSubTask2);

        Epic secondEpic = new Epic("Учиться", "Выполнить задания по учёбе", "NEW");
        int epic2 = taskManager.newEpic(secondEpic);

        SubTask secondEpicSubTask = new SubTask("Проектная деятельность",
                "выполнить тз для Яндекс Практикума", "NEW", secondEpic);
        int subTask3 = taskManager.newSubTask(secondEpicSubTask);

        System.out.println(taskManager.getAllTasks());

        firstTask.setStatus(Status.IN_PROGRESS.toString());

        /* В ревью было написано *эпики статусом самостоятельно не управляют.*, но ведь у меня как-раз так и сделано.
        Я полагаю я не совсем понятно назвал объекты представляющие соотвествующие задачи, но в последующем коде
        у меня меняется статус исключительно у трёх SubTask объектов. После чего вызываются методы для обнавления
        статуса самих сабтасков и их эпиков
         */


        firstEpicSubTask.setStatus(Status.DONE.toString());
        firstEpicSubTask2.setStatus(Status.DONE.toString());
        secondEpicSubTask.setStatus(Status.DONE.toString());

        taskManager.updateTask(firstTask);
        taskManager.updateSubTask(firstEpicSubTask);
        taskManager.updateSubTask(secondEpicSubTask);

        System.out.println(taskManager.getAllTasks());

        taskManager.deleteTaskById(task1);
        taskManager.deleteEpicById(epic1);
        taskManager.deleteSubTaskById(subTask3);

        System.out.println(taskManager.getAllTasks());

        System.out.println(taskManager.getEpicById(epic2));

        taskManager.deleteAll();

        System.out.println(taskManager.getAllTasks());
    }
}
