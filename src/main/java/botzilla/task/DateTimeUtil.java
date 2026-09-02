package botzilla.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Utility for parsing user-entered dates/times in "d/M/yyyy" or
 * "d/M/yyyy HHmm" format, and formatting them for display or for
 * saving to disk.
 */
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
     * Attempts to parse the given text as a date or date-time, trying
     * "d/M/yyyy HHmm" first, then "d/M/yyyy". Returns empty if neither
     * format matches, so the caller can fall back to storing the text
     * as-is.
     *
     * @param text the raw user input to parse
     * @return the parsed LocalDateTime, or empty if unparseable
     */
    public static Optional<LocalDateTime> parse(String text) {
        String trimmed = text.trim();

        try {
            return Optional.of(LocalDateTime.parse(trimmed, FILE_FORMAT_WITH_TIME));
        } catch (DateTimeParseException ignored) {
            // Not in this format; fall through and try the next format
        }

        try {
            LocalDate date = LocalDate.parse(trimmed, FILE_FORMAT_DATE_ONLY);
            return Optional.of(date.atStartOfDay());
        } catch (DateTimeParseException ignored) {
            // Not in this format either; caller will treat as unparseable
        }

        return Optional.empty();
    }

    /**
     * Checks whether the given text (assumed already parseable by
     * {@link #parse}) includes a time component, as opposed to a
     * date-only value.
     *
     * @param text the raw user input to check
     * @return true if the text matches the "with time" format
     */
    public static boolean hasTimeComponent(String text) {
        try {
            LocalDateTime.parse(text.trim(), FILE_FORMAT_WITH_TIME);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Formats a date-time for display to the user, e.g.
     * "Dec 02 2019, 6:00PM" if hasTime is true, or "Dec 02 2019" if false.
     *
     * @param dateTime the date-time to format
     * @param hasTime  whether a time component should be included
     * @return the formatted display string
     */
    public static String formatForDisplay(LocalDateTime dateTime, boolean hasTime) {
        return hasTime ? dateTime.format(OUTPUT_WITH_TIME) : dateTime.format(OUTPUT_DATE_ONLY);
    }

    /**
     * Formats a date-time for saving to disk, matching the same
     * "d/M/yyyy [HHmm]" format accepted by {@link #parse}.
     *
     * @param dateTime the date-time to format
     * @param hasTime  whether a time component should be included
     * @return the formatted file string
     */
    public static String formatForFile(LocalDateTime dateTime, boolean hasTime) {
        return hasTime ? dateTime.format(FILE_FORMAT_WITH_TIME) : dateTime.format(FILE_FORMAT_DATE_ONLY);
    }
}
