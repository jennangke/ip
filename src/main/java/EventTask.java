public class EventTask extends Task {
    private String start;
    private String end;

    public EventTask(String name, String start, String end) {
        super(name, TaskType.EVENT);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return super.toString() + " (from " + this.start + " to " + this.end + ")";
    }

    @Override
    public String toFileString() {
        return super.toFileString() + " | " + this.start + " | " + this.end;
    }
}
