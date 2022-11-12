public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task firstTask = new Task("Заняться спортом", "Совершить утреннюю пробежку", "NEW");
        Task secondTask = new Task("Пообедать", "Приготовить обед", "NEW");

        Epic firstEpic = new Epic("Сходить в магазин", "Закупиться продуктами по списку", "NEW");
        SubTask firstEpicSubTask = new SubTask("Подготовить список",
                "подготовить список того что нужно купить",
                "NEW", 0);
        SubTask firstEpicSubTask2 = new SubTask("Совершить покупки", "собрать корзину и оплатить товары",
                "NEW", 0);

        Epic secondEpic = new Epic("Учиться", "Выполнить задания по учёбе", "NEW");
        SubTask secondEpicSubTask = new SubTask("Проектная деятельность",
                "выполнить тз для Яндекс Практикума", "NEW", 1);

        taskManager.newTask(firstTask);
        taskManager.newTask(secondTask);

        taskManager.newEpic(firstEpic);
        taskManager.newSubTask(firstEpicSubTask);
        taskManager.newSubTask(firstEpicSubTask2);

        taskManager.newEpic(secondEpic);
        taskManager.newSubTask(secondEpicSubTask);

        System.out.println(taskManager.getAllTasks());

        firstTask.setStatus("IN_PROGRESS");
        firstEpicSubTask.setStatus("IN_PROGRESS");
        firstEpicSubTask2.setStatus("DONE");
        secondEpicSubTask.setStatus("DONE");

        taskManager.updateTask(firstTask.getId(), firstTask);
        taskManager.updateSubTask(firstEpicSubTask.getId(), firstEpicSubTask);
        taskManager.updateSubTask(secondEpicSubTask.getId(), secondEpicSubTask);

        System.out.println(taskManager.getAllTasks());

        taskManager.deleteTaskById(0);
        taskManager.deleteEpicById(1);
        taskManager.deleteSubTaskById(1);

        System.out.println(taskManager.getAllTasks());

        System.out.println(taskManager.getEpicById(0));

        taskManager.deleteAll();

        System.out.println(taskManager.getAllTasks());
    }
}
