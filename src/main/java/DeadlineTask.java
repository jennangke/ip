public class DeadlineTask extends Task {
    private String due;

    public DeadlineTask(String name, String due) {
        super(name);
        this.due = due;
    }

    @Override
    protected String getTaskType() {
        return "D";
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + this.due + ")";
    }
}
