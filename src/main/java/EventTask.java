public class EventTask extends Task {
    private String start;
    private String end;

    public EventTask(String name, String start, String end) {
        super(name);
        this.start = start;
        this.end = end;
    }

    @Override
    protected String getTaskType() {
        return "E";
    }

    @Override
    public String toString() {
        return super.toString() + " (from " + this.start + " to " + this.end + ")";
    }
}
