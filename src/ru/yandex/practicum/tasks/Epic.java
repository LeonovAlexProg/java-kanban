package ru.yandex.practicum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Epic extends Task{
    protected ArrayList<Integer> subTasks;
    protected LocalDateTime endTime;

    public Epic(String name, String info, Status status) {
        super(name, info, status);
        this.subTasks = new ArrayList<>();
    }

    public Epic(String name, String info, Status status, Duration duration, LocalDateTime startTime) {
        super(name, info, status, duration, startTime);
        this.subTasks = new ArrayList<>();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
        StringBuilder stringBuilder = new StringBuilder();
        if (startTime != null) {
            stringBuilder.append(" ,")
                    .append(startTime).append(",")
                    .append(duration).append(",")
                    .append(getEndTime());
        }
        return String.format("%d,%s,%s,%s,%s,%s%n", id, TaskTypes.EPIC, name, status, info, stringBuilder.toString());
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

//    public void setSubTasks(ArrayList<Integer> subTasks) {
//        this.subTasks = subTasks;
//    }
}
