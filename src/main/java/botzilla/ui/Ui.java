package botzilla.ui;

import botzilla.task.Task;
import botzilla.task.TaskList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Handles all console input and output for the chatbot: reading user
 * commands and printing responses, confirmations, and error messages.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BANNER =
            "    ____        __        _ ____     \n" +
                    "   / __ )____  / /_____  (_) / /___ _\n" +
                    "  / __  / __ \\/ __/_  / / / / / __ `/\n" +
                    " / /_/ / /_/ / /_  / /_/ / / / /_/ / \n" +
                    "/_____/\\____/\\__/ /___/_/_/_/\\__,_/  \n";

    private Scanner scanner;

    /**
     * Constructs a Ui that reads input from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints the startup banner and welcome greeting.
     */
    public void showWelcome() {
        System.out.println(DIVIDER);
        System.out.println(BANNER);
        System.out.println("Hello there! I'm Botzilla.");
        System.out.println("What can I do for you on this fine day?");
        System.out.println(DIVIDER);
    }

    /**
     * Prints the goodbye message shown when the user exits.
     */
    public void showGoodbye() {
        System.out.println("Cheers! Have a great day ahead!");
        System.out.println(DIVIDER);
    }


    /**
     * Prints a plain divider line, used to separate sections of output.
     */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Prints an error message for an invalid or unrecognized command.
     *
     * @param message the error description
     */public void showError(String message) {
        System.out.println(" HEY THERE!!! " + message);
        System.out.println(DIVIDER);
    }

    /**
     * Prints a warning shown when saved tasks fail to load on startup.
     */
    public void showLoadingError() {
        System.out.println(" HEY THERE!!! I couldn't load your saved tasks, starting fresh.");
        System.out.println(DIVIDER);
    }

    /**
     * Prints a confirmation after a task has been added.
     *
     * @param task      the task that was added
     * @param taskCount the total number of tasks after adding
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(" Gotcha. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" You have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /**
     * Prints a confirmation after a task has been deleted.
     *
     * @param task      the task that was removed
     * @param taskCount the total number of tasks after removal
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(" Gotcha. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" You have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /**
     * Prints the result of a mark/unmark operation.
     *
     * @param message the confirmation message returned by the task
     */
    public void showMarkResult(String message) {
        System.out.println(" " + message);
        System.out.println(DIVIDER);
    }

    /**
     * Prints the full list of tasks, numbered from 1.
     *
     * @param tasks the task list to display
     */
    public void showList(TaskList tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER);
    }

    /**
     * Prints the deadlines/events occurring on a given date, or a
     * fallback message if none are found.
     *
     * @param date    the queried date
     * @param matches the tasks occurring on that date
     */
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

    /**
     * Reads a single line of user input from standard input.
     *
     * @return the raw input text
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Closes the underlying input scanner. Should be called once when
     * the application is shutting down.
     */
    public void close() {
        scanner.close();
    }
}
