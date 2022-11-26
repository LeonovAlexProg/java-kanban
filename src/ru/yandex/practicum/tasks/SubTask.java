package ru.yandex.practicum.tasks;

public class SubTask extends Task{
    protected int epicId;

    public SubTask(String name, String info, Status status, Epic epic) {
        super(name, info, status);

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

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
