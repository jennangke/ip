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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5)) - 1;
                System.out.println(" " + tasks[index].mark());
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                System.out.println(" " + tasks[index].unmark());
                System.out.println("____________________________________________________________");
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println("task added: " + input);
                System.out.println("____________________________________________________________");
            }
        }

        System.out.println("Cheers! Have a great day!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }
}