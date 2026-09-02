package botzilla.task;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Represents a task that occurs over a specific time range,
 * e.g. "project meeting /from 2/12/2019 1400 /to 2/12/2019 1600".
 * If either date can't be parsed, it is stored and displayed as
 * plain text instead.
 */
public class EventTask extends Task {
    private LocalDateTime start;
    private LocalDateTime end;
    private String startRaw;
    private String endRaw;
    private boolean startHasTime;
    private boolean endHasTime;

    /**
     * Constructs a new EventTask, attempting to parse the given start
     * and end text as dates or date-times. Falls back to storing either
     * as raw text if parsing fails.
     *
     * @param name Display name/description of the task.
     * @param start Event start text, e.g. "2/12/2019 1400" or free text.
     * @param end Event end text, e.g. "2/12/2019 1600" or free text.
     */
    public EventTask(String name, String start, String end) {
        super(name, TaskType.EVENT);

        Optional<LocalDateTime> parsedStart = DateTimeUtil.parse(start);
        if (parsedStart.isPresent()) {
            this.start = parsedStart.get();
            startHasTime = DateTimeUtil.hasTimeComponent(start);
        } else {
            startRaw = start;
        }

        Optional<LocalDateTime> parsedEnd = DateTimeUtil.parse(end);
        if (parsedEnd.isPresent()) {
            this.end = parsedEnd.get();
            endHasTime = DateTimeUtil.hasTimeComponent(end);
        } else {
            endRaw = end;
        }
    }

    /**
     * Returns this task, including its start and end, serialized for
     * saving to disk, with appended "| start | end" fields.
     */
    @Override
    public String toFileString() {
        String startText = (start != null) ? DateTimeUtil.formatForFile(start, startHasTime) : startRaw;
        String endText = (end != null) ? DateTimeUtil.formatForFile(end, endHasTime) : endRaw;
        return super.toFileString() + " | " + startText + " | " + endText;
    }

    /**
     * Returns a human-readable representation including the start and
     * end times, e.g. "[E][ ] meeting (from: ... to: ...)".
     */
    @Override
    public String toString() {
        String displayStart = (start != null) ? DateTimeUtil.formatForDisplay(start, startHasTime) : startRaw;
        String displayEnd = (end != null) ? DateTimeUtil.formatForDisplay(end, endHasTime) : endRaw;
        return super.toString() + " (from: " + displayStart + " to: " + displayEnd + ")";
    }

    /**
     * Returns the parsed start time as a LocalDateTime, or null if the
     * original input couldn't be parsed as a date.
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Returns the parsed end time as a LocalDateTime, or null if the
     * original input couldn't be parsed as a date.
     */
    public LocalDateTime getEnd() {
        return end;
    }
}
