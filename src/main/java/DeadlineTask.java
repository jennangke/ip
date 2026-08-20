public class DeadlineTask extends Task {
    private String due;

    public DeadlineTask(String name, String due) {
        super(name, TaskType.DEADLINE);
        this.due = due;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + this.due + ")";
    }
}
