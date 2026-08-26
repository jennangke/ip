import java.time.LocalDate;
import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task remove(int index) {
        return tasks.remove(index);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> getAll() {
        return tasks;
    }

    public ArrayList<Task> getTasksOnDate(LocalDate date) {
        ArrayList<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task instanceof DeadlineTask) {
                DeadlineTask d = (DeadlineTask) task;
                if (d.getBy() != null && d.getBy().toLocalDate().equals(date)) {
                    result.add(task);
                }
            } else if (task instanceof EventTask) {
                EventTask e = (EventTask) task;
                if (e.getStart() != null && e.getStart().toLocalDate().equals(date)) {
                    result.add(task);
                }
            }
        }
        return result;
    }
}