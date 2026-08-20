public class ToDoTask extends Task {

    public ToDoTask(String name) {
        super(name);
    }

    @Override
    protected String getTaskType() {
        return "T";
    }

}
