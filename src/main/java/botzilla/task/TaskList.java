package botzilla.task;


import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Wraps a list of tasks and provides operations to add, remove, and
 * query them, including finding deadlines/events on a specific date.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList wrapping an existing list of tasks,
     * typically loaded from disk.
     *
     * @param tasks the initial list of tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index zero-based index of the task to remove
     * @return the removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given index.
     *
     * @param index zero-based index of the task
     * @return the task at that index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list of all tasks.
     *
     * @return the full task list
     */
    public ArrayList<Task> getAll() {
        return tasks;
    }

    /**
     * Finds all deadlines and events whose date falls on the given day.
     * Tasks with unparseable dates (stored as raw text) are excluded,
     * since they have no date to compare against.
     *
     * @param date the target calendar date
     * @return a list of matching tasks, in their original order
     */
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