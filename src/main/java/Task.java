public abstract class Task {
    private String name;
    private boolean isDone;
    private TaskType type;

    public Task(String name, TaskType type) {
        this.name = name;
        this.isDone = false;
        this.type = type;
    }

    public String mark() {
        isDone = true;
        return this.name + " marked as done! You go girl!";
    }

    public String unmark() {
        isDone = false;
        return this.name + " is now marked as not done! Keep pushing on!";
    }

    @Override
    public String toString() {
            return "[" + type.getIcon() + "][" + getTaskStatus() + "] " + this.name;
    }

    protected String getTaskStatus() {
        return isDone ? "X" : " ";
    }

    public String toFileString() {
        return type.getIcon() + " | " + (isDone ? "1" : "0") + " | " + this.name;
    }


}

