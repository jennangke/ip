public abstract class Task {
    private String name;
    private boolean isDone;

    public Task(String name) {
        this.name = name;
        this.isDone = false;
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
            return "[" + getTaskType() + "]" + "[" + getTaskStatus() + "] " + this.name;
    }

    protected String getTaskStatus() {
        return isDone ? "X" : " ";
    }

    protected abstract String getTaskType();
}

