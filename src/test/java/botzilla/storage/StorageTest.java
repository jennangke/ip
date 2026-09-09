package botzilla.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import botzilla.BotzillaException;
import botzilla.task.DeadlineTask;
import botzilla.task.EventTask;
import botzilla.task.Task;
import botzilla.task.ToDoTask;

/**
 * Tests that tags survive a save/load round trip through the save file,
 * and that save files written before tags existed (with no trailing
 * tags field) still load correctly.
 */
public class StorageTest {

    @TempDir
    Path tempDir;

    private Storage newStorage() {
        return new Storage(tempDir.resolve("botzilla.txt").toString());
    }

    @Test
    void saveThenLoad_todoWithTags_preservesTags() throws BotzillaException {
        Storage storage = newStorage();
        Task task = new ToDoTask("read book");
        task.addTag("fun");
        task.addTag("easy");

        storage.save(new ArrayList<>(List.of(task)));
        ArrayList<Task> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertEquals(Set.of("fun", "easy"), loaded.get(0).getTags());
    }

    @Test
    void saveThenLoad_deadlineWithTags_preservesTagsAndDate() throws BotzillaException {
        Storage storage = newStorage();
        Task task = new DeadlineTask("return book", "2/12/2019 1800");
        task.addTag("urgent");

        storage.save(new ArrayList<>(List.of(task)));
        ArrayList<Task> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertEquals(Set.of("urgent"), loaded.get(0).getTags());
        assertTrue(loaded.get(0).getDate().isPresent());
    }

    @Test
    void saveThenLoad_eventWithTags_preservesTagsAndBothDates() throws BotzillaException {
        Storage storage = newStorage();
        Task task = new EventTask("party", "1/1/2026 1800", "1/1/2026 2200");
        task.addTag("fun");
        task.addTag("social");

        storage.save(new ArrayList<>(List.of(task)));
        ArrayList<Task> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertEquals(Set.of("fun", "social"), loaded.get(0).getTags());
        assertTrue(loaded.get(0).getDate().isPresent());
    }

    @Test
    void saveThenLoad_taskWithoutTags_loadsWithEmptyTags() throws BotzillaException {
        Storage storage = newStorage();
        Task task = new ToDoTask("read book");

        storage.save(new ArrayList<>(List.of(task)));
        ArrayList<Task> loaded = storage.load();

        assertTrue(loaded.get(0).getTags().isEmpty());
    }

    @Test
    void load_oldFormatLineWithoutTagsField_stillLoadsWithEmptyTags() throws BotzillaException, IOException {
        Path file = tempDir.resolve("botzilla.txt");
        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            // Pre-tags save format: no trailing tags column.
            writer.write("D | 1 | return book | 2/12/2019 1800\n");
        }
        Storage storage = new Storage(file.toString());

        ArrayList<Task> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertTrue(loaded.get(0).getTags().isEmpty());
    }
}
