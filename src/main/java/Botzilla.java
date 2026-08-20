import java.util.Scanner;

public class Botzilla {
    public static void main(String[] args) {
        String banner =
                "    ____        __        _ ____     \n" +
                        "   / __ )____  / /_____  (_) / /___ _\n" +
                        "  / __  / __ \\/ __/_  / / / / / __ `/\n" +
                        " / /_/ / /_/ / /_  / /_/ / / / /_/ / \n" +
                        "/_____/\\____/\\__/ /___/_/_/_/\\__,_/  \n";

        System.out.println("____________________________________________________________");
        System.out.println(banner);
        System.out.println("Hello there! I'm Botzilla.");
        System.out.println("What can I do for you on this fine day?");
        System.out.println("____________________________________________________________");

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) {
                markTask(tasks, taskCount, input.substring(5), true);
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("unmark ")) {
                markTask(tasks, taskCount, input.substring(7), false);
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("todo ")) {
                String name = input.substring(5);
                tasks[taskCount] = new ToDoTask(name);
                taskCount++;
                printAdded(tasks[taskCount - 1], taskCount);
            } else if (input.startsWith("deadline ")) {
                String rest = input.substring(9);
                String[] parts = rest.split(" /by ", 2);
                tasks[taskCount] = new DeadlineTask(parts[0], parts[1]);
                taskCount++;
                printAdded(tasks[taskCount - 1], taskCount);
            } else if (input.startsWith("event ")) {
                String rest = input.substring(6);
                String[] fromSplit = rest.split(" /from ", 2);
                String[] toSplit = fromSplit[1].split(" /to ", 2);
                tasks[taskCount] = new EventTask(fromSplit[0], toSplit[0], toSplit[1]);
                taskCount++;
                printAdded(tasks[taskCount - 1], taskCount);
            } else {
                System.out.println(" I'm sorry, I don't understand that command.");
                System.out.println("____________________________________________________________");
            }
        }

        System.out.println("Cheers! Have a great day ahead!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }

    private static void printAdded(Task task, int taskCount) {
        System.out.println(" Gotcha! I've added this task:");
        System.out.println("   " + task);
        System.out.println(" You have " + taskCount + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    private static void markTask(Task[] tasks, int taskCount, String numberText, boolean markAsDone) {
        try {
            int index = Integer.parseInt(numberText) - 1;

            if (index < 0 || index >= taskCount) {
                System.out.println(" Hmm, I don't have a task numbered " + numberText + ".");
                return;
            }

            if (markAsDone) {
                System.out.println(" " + tasks[index].mark());
            } else {
                System.out.println(" " + tasks[index].unmark());
            }
        } catch (NumberFormatException e) {
            System.out.println(" Please give me a valid task number, e.g. mark 1");
        }
    }
}