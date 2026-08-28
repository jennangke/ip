package botzilla.task;

/**
 * Represents a single task in the task list. Holds the task's name,
 * completion status, and type. Subclasses (ToDoTask, DeadlineTask,
 * EventTask) add type-specific fields such as dates.
 */
public abstract class Task {
    private String name;
    private boolean isDone;
    private TaskType type;

    /**
     * Constructs a new, not-done Task with the given name and type.
     *
     * @param name display name/description of the task
     * @param type the task's type (todo, deadline, or event)
     */
    public Task(String name, TaskType type) {
        this.name = name;
        this.isDone = false;
        this.type = type;
    }

    /**
     * Marks this task as done.
     *
     * @return a confirmation message describing the change
     */
    public String mark() {
        isDone = true;
        return this.name + " marked as done! You go girl!";
    }

    /**
     * Marks this task as not done.
     *
     * @return a confirmation message describing the change
     */
    public String unmark() {
        isDone = false;
        return this.name + " is now marked as not done! Keep pushing on!";
    }

    /**
     * Returns a human-readable representation of this task for display,
     * e.g. "[T][X] read book". Subclasses append their own extra details.
     *
     * @return the formatted task string
     */
    @Override
    public String toString() {
            return "[" + type.getIcon() + "][" + getTaskStatus() + "] " + this.name;
    }

    /**
     * Returns the single-character icon representing completion status.
     *
     * @return "X" if done, otherwise a blank space
     */
    protected String getTaskStatus() {
        return isDone ? "X" : " ";
    }

    /**
     * Serializes this task into the pipe-delimited format used for saving
     * to disk. Subclasses append their own extra fields to this base string.
     *
     * @return the task encoded as "<type icon> | <done flag> | <name>"
     */
    public String toFileString() {
        return type.getIcon() + " | " + (isDone ? "1" : "0") + " | " + this.name;
    }


}

