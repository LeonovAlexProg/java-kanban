public class SubTask extends Task{
    protected int epicId;

    public SubTask(String name, String info, String status, int epicId) {
        super(name, info, status);
        this.epicId = epicId;
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
