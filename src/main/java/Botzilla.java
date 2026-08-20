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
        String[] tasks = new String[100];
        int count = 0;

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                for (int i = 0; i < count; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else {
                tasks[count] = input;
                count++;
                System.out.println("item added: " + input);
                System.out.println("____________________________________________________________");
            }
        }

        System.out.println("Cheers! Have a great day!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }
}