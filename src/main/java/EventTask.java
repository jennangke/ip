import java.time.LocalDateTime;
import java.util.Optional;

public class EventTask extends Task {
    private LocalDateTime start;
    private LocalDateTime end;
    private String startRaw;
    private String endRaw;
    private boolean startHasTime;
    private boolean endHasTime;

    public EventTask(String name, String start, String end) {
        super(name, TaskType.EVENT);

        Optional<LocalDateTime> parsedStart = DateTimeUtil.parse(start);
        if (parsedStart.isPresent()) {
            this.start = parsedStart.get();
            this.startHasTime = DateTimeUtil.hasTimeComponent(start);
        } else {
            this.startRaw = start;
        }

        Optional<LocalDateTime> parsedEnd = DateTimeUtil.parse(end);
        if (parsedEnd.isPresent()) {
            this.end = parsedEnd.get();
            this.endHasTime = DateTimeUtil.hasTimeComponent(end);
        } else {
            this.endRaw = end;
        }
    }

    @Override
    public String toFileString() {
        String startText = (start != null) ? DateTimeUtil.formatForFile(start, startHasTime) : startRaw;
        String endText = (end != null) ? DateTimeUtil.formatForFile(end, endHasTime) : endRaw;
        return super.toFileString() + " | " + startText + " | " + endText;
    }

    @Override
    public String toString() {
        String displayStart = (start != null) ? DateTimeUtil.formatForDisplay(start, startHasTime) : startRaw;
        String displayEnd = (end != null) ? DateTimeUtil.formatForDisplay(end, endHasTime) : endRaw;
        return super.toString() + " (from: " + displayStart + " to: " + displayEnd + ")";
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}