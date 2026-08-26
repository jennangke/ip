import java.time.LocalDateTime;
import java.util.Optional;

public class DeadlineTask extends Task {
    private LocalDateTime by;
    private String byRaw;
    private boolean byHasTime;

    public DeadlineTask(String name, String by) {
        super(name, TaskType.DEADLINE);
        Optional<LocalDateTime> parsed = DateTimeUtil.parse(by);
        if (parsed.isPresent()) {
            this.by = parsed.get();
            this.byHasTime = DateTimeUtil.hasTimeComponent(by);
            this.byRaw = null;
        } else {
            this.by = null;
            this.byRaw = by;
        }
    }

    @Override
    public String toFileString() {
        String byText = (by != null) ? DateTimeUtil.formatForFile(by, byHasTime) : byRaw;
        return super.toFileString() + " | " + byText;
    }

    @Override
    public String toString() {
        String displayBy = (by != null) ? DateTimeUtil.formatForDisplay(by, byHasTime) : byRaw;
        return super.toString() + " (by: " + displayBy + ")";
    }

    public LocalDateTime getBy() {
        return by;
    }
}