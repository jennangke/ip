public class Task {
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
        if (isDone) {
            return "[X] " + this.name;
        } else {
            return "[ ] " + this.name;
        }
    }
}

