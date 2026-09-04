package botzilla;

import java.time.LocalDate;

import botzilla.parser.Parser;
import botzilla.storage.Storage;
import botzilla.task.Task;
import botzilla.task.TaskList;
import botzilla.ui.Ui;

/**
 * Entry point for the Botzilla chatbot application.
 * Wires together the Ui, Storage, Parser, and TaskList components
 * and runs the main command loop.
 */
public class Botzilla {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a Botzilla instance, loading any previously saved tasks
     * from the given file path. If loading fails, starts with an empty
     * task list and notifies the user via the Ui.
     *
     * @param filePath Path to the file used for loading and saving tasks.
     */
    public Botzilla(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (BotzillaException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main command loop, reading user input and dispatching it
     * to {@link #executeCommand} until the user issues "bye".
     */
    public void run() {
        ui.showWelcome();

        Parser.CommandType type;
        do {
            String input = ui.readCommand();
            type = Parser.parseCommandType(input);

            try {
                System.out.println(executeCommand(type, input));
            } catch (BotzillaException e) {
                System.out.println(ui.formatError(e.getMessage()));
            }
            ui.showLine();
        } while (type != Parser.CommandType.BYE);

        ui.close();
    }

    /**
     * Returns Botzilla's opening greeting. Used by the GUI to display an
     * initial message when the window first opens, mirroring the welcome
     * banner shown at the start of the console loop.
     *
     * @return Botzilla's greeting message.
     */
    public String getGreeting() {
        return ui.formatGreeting();
    }

    /**
     * Returns whether the given input is Botzilla's exit command. Used by
     * the GUI to know when to close the window after showing the goodbye
     * message.
     *
     * @param input Raw user input.
     * @return True if the input is the "bye" command.
     */
    public boolean isExit(String input) {
        return Parser.parseCommandType(input) == Parser.CommandType.BYE;
    }

    /**
     * Generates Botzilla's response to a single line of user input. Used by
     * the GUI, which calls this once per message instead of running the
     * console read-eval-print loop.
     *
     * @param input Raw user input.
     * @return Botzilla's reply, ready to display.
     */
    public String getResponse(String input) {
        Parser.CommandType type = Parser.parseCommandType(input);
        try {
            return executeCommand(type, input);
        } catch (BotzillaException e) {
            return ui.formatError(e.getMessage());
        }
    }

    /**
     * Executes a single parsed command against the current task list,
     * saving to disk as needed, and returns the response text to display.
     *
     * @param type Kind of command to execute.
     * @param input Raw user input, used to extract command arguments.
     * @return The response text describing the result of the command.
     * @throws BotzillaException If the command's arguments are invalid.
     */
    private String executeCommand(Parser.CommandType type, String input) throws BotzillaException {
        switch (type) {
            case BYE:
                return ui.formatGoodbye();
            case LIST:
                return ui.formatList(tasks);
            case MARK: {
                int index = Parser.parseTaskNumber(input.substring(5), tasks.size());
                Task task = tasks.get(index);
                String result = ui.formatMarkResult(task.mark());
                storage.save(tasks.getAll());
                return result;
            }
            case UNMARK: {
                int index = Parser.parseTaskNumber(input.substring(7), tasks.size());
                Task task = tasks.get(index);
                String result = ui.formatMarkResult(task.unmark());
                storage.save(tasks.getAll());
                return result;
            }
            case DELETE: {
                String numberText = input.length() > 6 ? input.substring(6).trim() : "";
                int index = Parser.parseTaskNumber(numberText, tasks.size());
                Task removed = tasks.remove(index);
                storage.save(tasks.getAll());
                return ui.formatTaskDeleted(removed, tasks.size());
            }
            case ON: {
                LocalDate targetDate = Parser.parseOnDate(input);
                return ui.formatOnDate(targetDate, tasks.getTasksOnDate(targetDate));
            }
            case TODO: {
                Task task = Parser.parseTodo(input);
                tasks.add(task);
                storage.save(tasks.getAll());
                return ui.formatTaskAdded(task, tasks.size());
            }
            case DEADLINE: {
                Task task = Parser.parseDeadline(input);
                tasks.add(task);
                storage.save(tasks.getAll());
                return ui.formatTaskAdded(task, tasks.size());
            }
            case EVENT: {
                Task task = Parser.parseEvent(input);
                tasks.add(task);
                storage.save(tasks.getAll());
                return ui.formatTaskAdded(task, tasks.size());
            }
            case FIND: {
                String keyword = Parser.parseFindKeyword(input);
                return ui.formatFindResults(tasks.findTasks(keyword));
            }
            default:
                throw new BotzillaException("Sorry bestie I don't know what that means :(");
        }
    }

    /**
     * Starts the Botzilla application, using "./data/botzilla.txt" as the
     * default save file location.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Botzilla("./data/botzilla.txt").run();
    }
}
