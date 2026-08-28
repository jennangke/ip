package botzilla.task;

/**
 * Represents a simple task with no associated date, e.g. "read book".
 */
public class ToDoTask extends Task {

    /**
     * Constructs a new ToDoTask with the given name.
     *
     * @param name display name/description of the task
     */
    public ToDoTask(String name) {
        super(name, TaskType.TODO);
    }

}
