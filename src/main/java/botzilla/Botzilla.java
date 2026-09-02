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

        while (true) {
            String input = ui.readCommand();
            Parser.CommandType type = Parser.parseCommandType(input);

            try {
                if (executeCommand(type, input)) {
                    break;
                }
            } catch (BotzillaException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    /**
     * Executes a single parsed command against the current task list,
     * saving to disk and printing results via the Ui as needed.
     *
     * @param type Kind of command to execute.
     * @param input Raw user input, used to extract command arguments.
     * @return True if the command was "bye" and the loop should stop.
     * @throws BotzillaException If the command's arguments are invalid.
     */
    private boolean executeCommand(Parser.CommandType type, String input) throws BotzillaException {
        switch (type) {
            case BYE:
                ui.showGoodbye();
                return true;
            case LIST:
                ui.showList(tasks);
                return false;
            case MARK: {
                int index = Parser.parseTaskNumber(input.substring(5), tasks.size());
                Task task = tasks.get(index);
                ui.showMarkResult(task.mark());
                storage.save(tasks.getAll());
                return false;
            }
            case UNMARK: {
                int index = Parser.parseTaskNumber(input.substring(7), tasks.size());
                Task task = tasks.get(index);
                ui.showMarkResult(task.unmark());
                storage.save(tasks.getAll());
                return false;
            }
            case DELETE: {
                String numberText = input.length() > 6 ? input.substring(6).trim() : "";
                int index = Parser.parseTaskNumber(numberText, tasks.size());
                Task removed = tasks.remove(index);
                storage.save(tasks.getAll());
                ui.showTaskDeleted(removed, tasks.size());
                return false;
            }
            case ON: {
                LocalDate targetDate = Parser.parseOnDate(input);
                ui.showOnDate(targetDate, tasks.getTasksOnDate(targetDate));
                return false;
            }
            case TODO: {
                Task task = Parser.parseTodo(input);
                tasks.add(task);
                storage.save(tasks.getAll());
                ui.showTaskAdded(task, tasks.size());
                return false;
            }
            case DEADLINE: {
                Task task = Parser.parseDeadline(input);
                tasks.add(task);
                storage.save(tasks.getAll());
                ui.showTaskAdded(task, tasks.size());
                return false;
            }
            case EVENT: {
                Task task = Parser.parseEvent(input);
                tasks.add(task);
                storage.save(tasks.getAll());
                ui.showTaskAdded(task, tasks.size());
                return false;
            }
            case FIND: {
                String keyword = Parser.parseFindKeyword(input);
                ui.showFindResults(tasks.findTasks(keyword));
                return false;
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
