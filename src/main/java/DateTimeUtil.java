import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateTimeUtil {
    private static final DateTimeFormatter OUTPUT_WITH_TIME =
            DateTimeFormatter.ofPattern("dd MMM yyyy, h:mma");
    private static final DateTimeFormatter OUTPUT_DATE_ONLY =
            DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter FILE_FORMAT_WITH_TIME =
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter FILE_FORMAT_DATE_ONLY =
            DateTimeFormatter.ofPattern("d/M/yyyy");

    /**
     * Tries to parse user input as a date or date-time.
     * Returns empty if it doesn't match "d/M/yyyy HHmm" or "d/M/yyyy",
     * so the caller can fall back to treating the text as a plain String.
     * Use hasTimeComponent() on the same text to check if a time was included.
     */
    public static Optional<LocalDateTime> parse(String text) {
        String trimmed = text.trim();

        try {
            return Optional.of(LocalDateTime.parse(trimmed, FILE_FORMAT_WITH_TIME));
        } catch (DateTimeParseException ignored) {
        }

        try {
            LocalDate date = LocalDate.parse(trimmed, FILE_FORMAT_DATE_ONLY);
            return Optional.of(date.atStartOfDay());
        } catch (DateTimeParseException ignored) {
        }

        return Optional.empty();
    }

    /**
     * Checks whether the given text (already confirmed parseable by parse())
     * matched the "with time" pattern specifically, as opposed to date-only.
     */
    public static boolean hasTimeComponent(String text) {
        try {
            LocalDateTime.parse(text.trim(), FILE_FORMAT_WITH_TIME);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String formatForDisplay(LocalDateTime dateTime, boolean hasTime) {
        return hasTime ? dateTime.format(OUTPUT_WITH_TIME) : dateTime.format(OUTPUT_DATE_ONLY);
    }

    public static String formatForFile(LocalDateTime dateTime, boolean hasTime) {
        return hasTime ? dateTime.format(FILE_FORMAT_WITH_TIME) : dateTime.format(FILE_FORMAT_DATE_ONLY);
    }
}