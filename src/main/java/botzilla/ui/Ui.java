package botzilla.ui;

import botzilla.task.Task;
import botzilla.task.TaskList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BANNER =
            "    ____        __        _ ____     \n" +
                    "   / __ )____  / /_____  (_) / /___ _\n" +
                    "  / __  / __ \\/ __/_  / / / / / __ `/\n" +
                    " / /_/ / /_/ / /_  / /_/ / / / /_/ / \n" +
                    "/_____/\\____/\\__/ /___/_/_/_/\\__,_/  \n";

    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println(DIVIDER);
        System.out.println(BANNER);
        System.out.println("Hello there! I'm Botzilla.");
        System.out.println("What can I do for you on this fine day?");
        System.out.println(DIVIDER);
    }

    public void showGoodbye() {
        System.out.println("Cheers! Have a great day ahead!");
        System.out.println(DIVIDER);
    }

    public void showLine() {
        System.out.println(DIVIDER);
    }

    public void showError(String message) {
        System.out.println(" HEY THERE!!! " + message);
        System.out.println(DIVIDER);
    }

    public void showLoadingError() {
        System.out.println(" HEY THERE!!! I couldn't load your saved tasks, starting fresh.");
        System.out.println(DIVIDER);
    }

    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(" Gotcha. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" You have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(" Gotcha. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" You have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    public void showMarkResult(String message) {
        System.out.println(" " + message);
        System.out.println(DIVIDER);
    }

    public void showList(TaskList tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER);
    }

    public void showOnDate(LocalDate date, java.util.List<Task> matches) {
        System.out.println(" Okay bestie, here's what's on "
                + date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) + ":");
        if (matches.isEmpty()) {
            System.out.println(" All clear! You are free as a lark!");
        } else {
            for (Task task : matches) {
                System.out.println(" " + task);
            }
        }
        System.out.println(DIVIDER);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}
