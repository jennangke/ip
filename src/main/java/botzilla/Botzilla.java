package botzilla;

import botzilla.parser.Parser;
import botzilla.storage.Storage;
import botzilla.task.Task;
import botzilla.task.TaskList;
import botzilla.ui.Ui;

import java.time.LocalDate;

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
     * @param filePath path to the file used for loading and saving tasks
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
     * Runs the main command loop: reads user input, dispatches it to the
     * appropriate handler based on the parsed command type, and prints
     * results or errors via the Ui. Loops until the user issues "bye".
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            Parser.CommandType type = Parser.parseCommandType(input);

            try {
                switch (type) {
                    case BYE:
                        ui.showGoodbye();
                        ui.close();
                        return;
                    case LIST:
                        ui.showList(tasks);
                        break;
                    case MARK: {
                        int index = Parser.parseTaskNumber(input.substring(5), tasks.size());
                        Task task = tasks.get(index);
                        ui.showMarkResult(task.mark());
                        storage.save(tasks.getAll());
                        break;
                    }
                    case UNMARK: {
                        int index = Parser.parseTaskNumber(input.substring(7), tasks.size());
                        Task task = tasks.get(index);
                        ui.showMarkResult(task.unmark());
                        storage.save(tasks.getAll());
                        break;
                    }
                    case DELETE: {
                        String numberText = input.length() > 6 ? input.substring(6).trim() : "";
                        int index = Parser.parseTaskNumber(numberText, tasks.size());
                        Task removed = tasks.remove(index);
                        storage.save(tasks.getAll());
                        ui.showTaskDeleted(removed, tasks.size());
                        break;
                    }
                    case ON: {
                        java.time.LocalDate targetDate = Parser.parseOnDate(input);
                        ui.showOnDate(targetDate, tasks.getTasksOnDate(targetDate));
                        break;
                    }
                    case TODO: {
                        Task task = Parser.parseTodo(input);
                        tasks.add(task);
                        storage.save(tasks.getAll());
                        ui.showTaskAdded(task, tasks.size());
                        break;
                    }
                    case DEADLINE: {
                        Task task = Parser.parseDeadline(input);
                        tasks.add(task);
                        storage.save(tasks.getAll());
                        ui.showTaskAdded(task, tasks.size());
                        break;
                    }
                    case EVENT: {
                        Task task = Parser.parseEvent(input);
                        tasks.add(task);
                        storage.save(tasks.getAll());
                        ui.showTaskAdded(task, tasks.size());
                        break;
                    }
                    default:
                        throw new BotzillaException("Sorry bestie I don't know what that means :(");
                }
            } catch (BotzillaException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Starts the Botzilla application, using "./data/botzilla.txt" as the
     * default save file location.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        new Botzilla("./data/botzilla.txt").run();
    }
}