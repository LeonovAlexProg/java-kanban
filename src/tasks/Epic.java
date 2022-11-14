package tasks;

import java.util.ArrayList;

public class Epic extends Task{
    protected ArrayList<Integer> subTasks;

    public Epic(String name, String info, String status) {
        super(name, info, status);
        this.subTasks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "subTasks=" + subTasks +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                "}\n";
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<Integer> subTasks) {
        this.subTasks = subTasks;
    }
}
