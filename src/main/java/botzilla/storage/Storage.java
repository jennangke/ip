package botzilla.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import botzilla.BotzillaException;
import botzilla.task.DeadlineTask;
import botzilla.task.EventTask;
import botzilla.task.Task;
import botzilla.task.ToDoTask;

/**
 * Handles reading tasks from, and writing tasks to, a save file on disk.
 */
public class Storage {
    private String filePath;

    /**
     * Constructs a Storage bound to the given file path.
     *
     * @param filePath path to the save file
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the save file. Returns an empty list if the file
     * doesn't exist yet (e.g. first run). Lines that can't be parsed are
     * skipped rather than causing a failure.
     *
     * @return the list of tasks loaded from disk
     * @throws BotzillaException if the file exists but can't be read
     */
    public ArrayList<Task> load() throws BotzillaException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new BotzillaException("Could not read saved tasks: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Parses a single line from the save file into a Task, based on its
     * pipe-delimited fields (type, done flag, name, and type-specific
     * fields). Returns null if the line is malformed or has an
     * unrecognized type code.
     *
     * @param line a single line from the save file
     * @return the parsed task, or null if the line couldn't be parsed
     */
    private Task parseLine(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }

        String typeCode = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String name = parts[2].trim();

        Task task;
        try {
            switch (typeCode) {
                case "T":
                    task = new ToDoTask(name);
                    break;
                case "D":
                    if (parts.length < 4) {
                        return null;
                    }
                    task = new DeadlineTask(name, parts[3].trim());
                    break;
                case "E":
                    if (parts.length < 5) {
                        return null;
                    }
                    task = new EventTask(name, parts[3].trim(), parts[4].trim());
                    break;
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }

        if (isDone) {
            task.mark();
        }

        return task;
    }

    /**
     * Saves the given list of tasks to the save file, overwriting any
     * previous contents. Creates the parent directory if it doesn't
     * already exist.
     *
     * @param tasks the current list of tasks to persist
     */
    public void save(ArrayList<Task> tasks) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks) {
                writer.write(task.toFileString() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println(" Warning: could not save tasks (" + e.getMessage() + ")");
        }
    }
}
