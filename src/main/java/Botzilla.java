import java.util.ArrayList;
import java.util.Scanner;

public class Botzilla {
    private static final String FILE_PATH = "./data/botzilla.txt";

    public static void main(String[] args) {
        String banner = "    ____        __        _ ____     \n" +
                "   / __ )____  / /_____  (_) / /___ _\n" +
                "  / __  / __ \\/ __/_  / / / / / __ `/\n" +
                " / /_/ / /_/ / /_  / /_/ / / / /_/ / \n" +
                "/_____/\\____/\\__/ /___/_/_/_/\\__,_/  \n";

        System.out.println("____________________________________________________________");
        System.out.println(banner);
        System.out.println("Hello there! I'm Botzilla.");
        System.out.println("What can I do for you on this fine day?");
        System.out.println("____________________________________________________________");

        Storage storage = new Storage(FILE_PATH);
        ArrayList<Task> tasks = storage.load();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            try {
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                    System.out.println("____________________________________________________________");
                } else if (input.startsWith("mark ")) {
                    markTask(tasks, input.substring(5), true);
                    storage.save(tasks);
                    System.out.println("____________________________________________________________");
                } else if (input.startsWith("unmark ")) {
                    markTask(tasks, input.substring(7), false);
                    storage.save(tasks);
                    System.out.println("____________________________________________________________");
                } else if (input.equals("delete") || input.startsWith("delete ")) {
                    deleteTask(tasks, input);
                    storage.save(tasks);
                    System.out.println("____________________________________________________________");
                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    tasks.add(parseTodo(input));
                    storage.save(tasks);
                    printAdded(tasks.get(tasks.size() - 1), tasks.size());
                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    tasks.add(parseDeadline(input));
                    storage.save(tasks);
                    printAdded(tasks.get(tasks.size() - 1), tasks.size());
                } else if (input.equals("event") || input.startsWith("event ")) {
                    tasks.add(parseEvent(input));
                    storage.save(tasks);
                    printAdded(tasks.get(tasks.size() - 1), tasks.size());
                } else {
                    throw new BotzillaException("Sorry bestie I don't know what that means :(");
                }
            } catch (BotzillaException e) {
                System.out.println(" HEY THERE!!! " + e.getMessage());
                System.out.println("____________________________________________________________");
            }
        }

        System.out.println("Cheers! Have a great day ahead!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }

    private static void printAdded(Task task, int taskCount) {
        System.out.println(" Gotcha. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" You have " + taskCount + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    private static void printDeleted(Task task, int taskCount) {
        System.out.println(" Gotcha. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" You have " + taskCount + " tasks in the list.");
    }

    private static void markTask(ArrayList<Task> tasks, String numberText, boolean markAsDone) throws BotzillaException {
        int index = parseTaskNumber(numberText, tasks.size());
        Task task = tasks.get(index);
        if (markAsDone) {
            System.out.println(" " + task.mark());
        } else {
            System.out.println(" " + task.unmark());
        }
    }

    private static void deleteTask(ArrayList<Task> tasks, String input) throws BotzillaException {
        String numberText = input.length() > 6 ? input.substring(6).trim() : "";
        int index = parseTaskNumber(numberText, tasks.size());
        Task removed = tasks.remove(index);
        printDeleted(removed, tasks.size());
    }

    private static int parseTaskNumber(String numberText, int taskCount) throws BotzillaException {
        int index;
        try {
            index = Integer.parseInt(numberText.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new BotzillaException("Please give me a valid task number, e.g. mark 1");
        }
        if (index < 0 || index >= taskCount) {
            throw new BotzillaException("I fear I don't have a task numbered " + numberText.trim() + ".");
        }
        return index;
    }

    private static Task parseTodo(String input) throws BotzillaException {
        String name = input.length() > 4 ? input.substring(4).trim() : "";
        if (name.isEmpty()) {
            throw new BotzillaException("Please give the todo a name! Description cannot be empty");
        }
        return new ToDoTask(name);
    }

    private static Task parseDeadline(String input) throws BotzillaException {
        String rest = input.length() > 8 ? input.substring(8).trim() : "";
        String[] parts = rest.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new BotzillaException("ADD A NAME, ADD A DATE! " +
                    "A deadline needs a description and a '/by' date, e.g. deadline return book /by Sunday");
        }
        return new DeadlineTask(parts[0].trim(), parts[1].trim());
    }

    private static Task parseEvent(String input) throws BotzillaException {
        String rest = input.length() > 5 ? input.substring(5).trim() : "";
        String[] fromSplit = rest.split(" /from ", 2);
        if (fromSplit.length < 2 || fromSplit[0].trim().isEmpty()) {
            throw new BotzillaException("ERROR ALERT! An event needs a description and '/from' and '/to' times, e.g. event meeting /from 2pm /to 4pm");
        }
        String[] toSplit = fromSplit[1].split(" /to ", 2);
        if (toSplit.length < 2 || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
            throw new BotzillaException("ERROR ALERT! An event needs a description and '/from' and '/to' times, e.g. event meeting /from 2pm /to 4pm");
        }
        return new EventTask(fromSplit[0].trim(), toSplit[0].trim(), toSplit[1].trim());
    }
}