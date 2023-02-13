package ru.yandex.practicum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected String name;
    protected String info;
    protected Integer id;
    protected Status status;

    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String info, Status status) {
        this.name = name;
        this.info = info;
        this.id = null;
        this.status = status;
    }

    public Task(String name, String info, Status status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.info = info;
        this.id = null;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (duration != null && startTime != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task that = (Task) o;

        return name.equals(that.name) && info.equals(that.info) && status.equals(that.status);
    }


    public String toCsvString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (startTime != null) {
            stringBuilder.append(" ,")
                    .append(startTime).append(",")
                    .append(duration).append(",")
                    .append(getEndTime());
        }
        return String.format("%d,%s,%s,%s,%s,%s%n", id, TaskTypes.TASK, name, status, info, stringBuilder.toString());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
