package botzilla.parser;

import botzilla.BotzillaException;
import botzilla.task.DateTimeUtil;
import botzilla.task.DeadlineTask;
import botzilla.task.EventTask;
import botzilla.task.Task;
import botzilla.task.ToDoTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Interprets raw user input: identifies which command was issued and
 * builds the corresponding Task, task index, or date from the input text.
 */
public class Parser {

    /**
     * The recognised kinds of user commands.
     */
    public enum CommandType {
        BYE, LIST, MARK, UNMARK, DELETE, ON, TODO, DEADLINE, EVENT, UNKNOWN
    }

    /**
     * Determines which command the given input represents, based on its
     * leading keyword.
     *
     * @param input the raw user input
     * @return the matching CommandType, or UNKNOWN if unrecognized
     */
    public static CommandType parseCommandType(String input) {
        if (input.equals("bye")) {
            return CommandType.BYE;
        } else if (input.equals("list")) {
            return CommandType.LIST;
        } else if (input.startsWith("mark ")) {
            return CommandType.MARK;
        } else if (input.startsWith("unmark ")) {
            return CommandType.UNMARK;
        } else if (input.equals("delete") || input.startsWith("delete ")) {
            return CommandType.DELETE;
        } else if (input.equals("on") || input.startsWith("on ")) {
            return CommandType.ON;
        } else if (input.equals("todo") || input.startsWith("todo ")) {
            return CommandType.TODO;
        } else if (input.equals("deadline") || input.startsWith("deadline ")) {
            return CommandType.DEADLINE;
        } else if (input.equals("event") || input.startsWith("event ")) {
            return CommandType.EVENT;
        } else {
            return CommandType.UNKNOWN;
        }
    }

    /**
     * Parses a 1-based task number from user input, validating that it
     * is numeric and refers to an existing task.
     *
     * @param numberText the raw number text, e.g. "1"
     * @param taskCount  the current number of tasks, for bounds checking
     * @return the corresponding 0-based task index
     * @throws BotzillaException if the text isn't a valid number or is
     *                           out of range
     */
    public static int parseTaskNumber(String numberText, int taskCount) throws BotzillaException {
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

    /**
     * Parses a "todo" command into a ToDoTask.
     *
     * @param input the raw user input, e.g. "todo read book"
     * @return the constructed ToDoTask
     * @throws BotzillaException if the description is empty
     */
    public static Task parseTodo(String input) throws BotzillaException {
        String name = input.length() > 4 ? input.substring(4).trim() : "";
        if (name.isEmpty()) {
            throw new BotzillaException("Please give the todo a name! Description cannot be empty");
        }
        return new ToDoTask(name);
    }

    /**
     * Parses a "deadline" command into a DeadlineTask.
     *
     * @param input the raw user input, e.g. "deadline return book /by 2/12/2019 1800"
     * @return the constructed DeadlineTask
     * @throws BotzillaException if the description or "/by" date is missing
     */
    public static Task parseDeadline(String input) throws BotzillaException {
        String rest = input.length() > 8 ? input.substring(8).trim() : "";
        String[] parts = rest.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new BotzillaException("ADD A NAME, ADD A DATE! " +
                    "A deadline needs a description and a '/by' date, e.g. deadline return book /by 2/12/2019 1800");
        }
        return new DeadlineTask(parts[0].trim(), parts[1].trim());
    }

    /**
     * Parses an "event" command into an EventTask.
     *
     * @param input the raw user input, e.g. "event meeting /from 2/12/2019 1400 /to 2/12/2019 1600"
     * @return the constructed EventTask
     * @throws BotzillaException if the description, "/from", or "/to" is missing
     */
    public static Task parseEvent(String input) throws BotzillaException {
        String rest = input.length() > 5 ? input.substring(5).trim() : "";
        String[] fromSplit = rest.split(" /from ", 2);
        if (fromSplit.length < 2 || fromSplit[0].trim().isEmpty()) {
            throw new BotzillaException("ERROR ALERT! An event needs a description and '/from' and '/to' times, e.g. event meeting /from 2/12/2019 1400 /to 2/12/2019 1600");
        }
        String[] toSplit = fromSplit[1].split(" /to ", 2);
        if (toSplit.length < 2 || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
            throw new BotzillaException("ERROR ALERT! An event needs a description and '/from' and '/to' times, e.g. event meeting /from 2/12/2019 1400 /to 2/12/2019 1600");
        }
        return new EventTask(fromSplit[0].trim(), toSplit[0].trim(), toSplit[1].trim());
    }

    /**
     * Parses an "on" command into a target date to query.
     *
     * @param input the raw user input, e.g. "on 2/12/2019"
     * @return the parsed LocalDate
     * @throws BotzillaException if no date is given or it can't be parsed
     */
    public static LocalDate parseOnDate(String input) throws BotzillaException {
        String dateText = input.length() > 2 ? input.substring(2).trim() : "";
        if (dateText.isEmpty()) {
            throw new BotzillaException("Please give me a date, e.g. on 2/12/2019");
        }

        Optional<LocalDateTime> parsed = DateTimeUtil.parse(dateText);
        if (parsed.isEmpty()) {
            throw new BotzillaException("I couldn't understand that date. Try a format like 2/12/2019.");
        }
        return parsed.get().toLocalDate();
    }
}