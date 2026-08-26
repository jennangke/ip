import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

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