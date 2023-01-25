package ru.yandex.practicum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Epic extends Task{
    protected ArrayList<Integer> subTasks;

    public Epic(String name, String info, Status status) {
        super(name, info, status);
        this.subTasks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasks +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                "}\n";
    }

    public String toCsvString() {
        return String.format("%d,%s,%s,%s,%s,%n", id, TaskTypes.EPIC, name, status, info);
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

//    public void setSubTasks(ArrayList<Integer> subTasks) {
//        this.subTasks = subTasks;
//    }
}
