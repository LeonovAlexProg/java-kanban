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
        return startTime.plus(duration);
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

    public String toCsvString() {
        return String.format("%d,%s,%s,%s,%s,%n", id, TaskTypes.TASK, name, status, info);
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
