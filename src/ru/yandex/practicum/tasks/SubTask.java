package ru.yandex.practicum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task{
    protected int epicId;

    public SubTask(String name, String info, Status status, Epic epic) {
        super(name, info, status);
        this.epicId = epic.getId();
    }

    public SubTask(String name, String info, Status status, Epic epic, Duration duration, LocalDateTime startTime) {
        super(name, info, status, duration, startTime);
        this.epicId = epic.getId();
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                "}\n";
    }

    public String toCsvString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (startTime != null) {
            stringBuilder.append(startTime).append(",");
            stringBuilder.append(duration).append(",");
            stringBuilder.append(getEndTime());
        }
        return String.format("%d,%s,%s,%s,%s,%s,%s%n", id, TaskTypes.SUBTASK, name, status, info, epicId,
                stringBuilder.toString());
       // return String.format("%d,%s,%s,%s,%s,%s%n", id, TaskTypes.SUBTASK, name, status, info, epicId);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
