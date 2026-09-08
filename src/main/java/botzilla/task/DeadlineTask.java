package botzilla.task;

import java.time.LocalDateTime;

/**
 * Represents a task that must be completed by a specific date/time,
 * e.g. "return book /by 2/12/2019 1800". If the given date text can't
 * be parsed, it is stored and displayed as plain text instead.
 */
public class DeadlineTask extends Task {
    private final FlexibleDateTime by;

    /**
     * Constructs a new DeadlineTask, attempting to parse the given
     * deadline text as a date or date-time. Falls back to storing it
     * as raw text if parsing fails.
     *
     * @param name Display name/description of the task.
     * @param by Deadline text, e.g. "2/12/2019 1800" or free text.
     */
    public DeadlineTask(String name, String by) {
        super(name, TaskType.DEADLINE);
        this.by = FlexibleDateTime.parse(by);
    }

    /**
     * Returns this task, including its deadline, serialized for saving
     * to disk, with an appended "| deadline" field.
     */
    @Override
    public String toFileString() {
        return super.toFileString() + " | " + by.toFileString();
    }

    /**
     * Returns a human-readable representation including the deadline,
     * e.g. "[D][ ] return book (by: Dec 02 2019, 6:00PM)".
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by.toDisplayString() + ")";
    }

    /**
     * Returns the parsed deadline as a LocalDateTime, or null if the
     * original input couldn't be parsed as a date.
     */
    public LocalDateTime getBy() {
        return by.toDateTimeOrNull();
    }
}
