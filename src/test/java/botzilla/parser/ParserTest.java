package botzilla.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import botzilla.BotzillaException;
import botzilla.task.Task;

public class ParserTest {

    // ---- BYE ----
    @Test
    void parseCommandType_bye_returnsBye() {
        assertEquals(Parser.CommandType.BYE, Parser.parseCommandType("bye"));
    }

    // ---- LIST ----
    @Test
    void parseCommandType_list_returnsList() {
        assertEquals(Parser.CommandType.LIST, Parser.parseCommandType("list"));
    }

    // ---- MARK ----
    @Test
    void parseCommandType_markWithArgument_returnsMark() {
        assertEquals(Parser.CommandType.MARK, Parser.parseCommandType("mark 1"));
    }

    @Test
    void parseCommandType_markWithoutTrailingSpace_returnsUnknown() {
        // "mark" alone does not match the "mark " prefix check
        assertEquals(Parser.CommandType.UNKNOWN, Parser.parseCommandType("mark"));
    }

    // ---- UNMARK ----
    @Test
    void parseCommandType_unmarkWithArgument_returnsUnmark() {
        assertEquals(Parser.CommandType.UNMARK, Parser.parseCommandType("unmark 2"));
    }

    @Test
    void parseCommandType_unmarkWithoutTrailingSpace_returnsUnknown() {
        assertEquals(Parser.CommandType.UNKNOWN, Parser.parseCommandType("unmark"));
    }

    // ---- DELETE ----
    @Test
    void parseCommandType_deleteAlone_returnsDelete() {
        assertEquals(Parser.CommandType.DELETE, Parser.parseCommandType("delete"));
    }

    @Test
    void parseCommandType_deleteWithArgument_returnsDelete() {
        assertEquals(Parser.CommandType.DELETE, Parser.parseCommandType("delete 3"));
    }

    // ---- ON ----
    @Test
    void parseCommandType_onAlone_returnsOn() {
        assertEquals(Parser.CommandType.ON, Parser.parseCommandType("on"));
    }

    @Test
    void parseCommandType_onWithArgument_returnsOn() {
        assertEquals(Parser.CommandType.ON, Parser.parseCommandType("on 2/12/2019"));
    }

    // ---- TODO ----
    @Test
    void parseCommandType_todoAlone_returnsTodo() {
        assertEquals(Parser.CommandType.TODO, Parser.parseCommandType("todo"));
    }

    @Test
    void parseCommandType_todoWithArgument_returnsTodo() {
        assertEquals(Parser.CommandType.TODO, Parser.parseCommandType("todo read book"));
    }

    // ---- DEADLINE ----
    @Test
    void parseCommandType_deadlineAlone_returnsDeadline() {
        assertEquals(Parser.CommandType.DEADLINE, Parser.parseCommandType("deadline"));
    }

    @Test
    void parseCommandType_deadlineWithArgument_returnsDeadline() {
        assertEquals(Parser.CommandType.DEADLINE,
                Parser.parseCommandType("deadline return book /by 2/12/2019 1800"));
    }

    // ---- EVENT ----
    @Test
    void parseCommandType_eventAlone_returnsEvent() {
        assertEquals(Parser.CommandType.EVENT, Parser.parseCommandType("event"));
    }

    @Test
    void parseCommandType_eventWithArgument_returnsEvent() {
        assertEquals(Parser.CommandType.EVENT,
                Parser.parseCommandType("event meeting /from 2/12/2019 1400 /to 2/12/2019 1600"));
    }

    // ---- UNKNOWN / edge cases ----
    @Test
    void parseCommandType_emptyString_returnsUnknown() {
        assertEquals(Parser.CommandType.UNKNOWN, Parser.parseCommandType(""));
    }

    @Test
    void parseCommandType_unrecognizedWord_returnsUnknown() {
        assertEquals(Parser.CommandType.UNKNOWN, Parser.parseCommandType("blah"));
    }

    @Test
    void parseCommandType_differentCasing_returnsUnknown() {
        // Matching is case-sensitive, so "Bye" should not match "bye"
        assertEquals(Parser.CommandType.UNKNOWN, Parser.parseCommandType("Bye"));
    }

    @Test
    void parseCommandType_leadingWhitespace_returnsUnknown() {
        // No trimming is performed, so leading whitespace breaks the match
        assertEquals(Parser.CommandType.UNKNOWN, Parser.parseCommandType(" bye"));
    }

    @Test
    void parseCommandType_substringOfKeyword_returnsUnknown() {
        // "lis" is not "list" and does not start with any recognized prefix
        assertEquals(Parser.CommandType.UNKNOWN, Parser.parseCommandType("lis"));
    }

    @Test
    void parseCommandType_keywordAsSubstringOfLongerWord_returnsUnknown() {
        // "listing" is neither equal to "list" nor followed by a space,
        // and does not match any startsWith("keyword ") prefix
        assertEquals(Parser.CommandType.UNKNOWN, Parser.parseCommandType("listing"));
    }

    // ---- TAG ----
    @Test
    void parseCommandType_tagWithArgument_returnsTag() {
        assertEquals(Parser.CommandType.TAG, Parser.parseCommandType("tag 1 fun"));
    }

    @Test
    void parseCommandType_tagWithoutTrailingSpace_returnsUnknown() {
        assertEquals(Parser.CommandType.UNKNOWN, Parser.parseCommandType("tag"));
    }

    // ---- UNTAG ----
    @Test
    void parseCommandType_untagWithArgument_returnsUntag() {
        assertEquals(Parser.CommandType.UNTAG, Parser.parseCommandType("untag 1 fun"));
    }

    @Test
    void parseCommandType_untagWithoutTrailingSpace_returnsUnknown() {
        assertEquals(Parser.CommandType.UNKNOWN, Parser.parseCommandType("untag"));
    }

    // ---- parseTagCommand() ----
    @Test
    void parseTagCommand_singleTag_returnsZeroBasedIndexAndTagName() throws BotzillaException {
        Parser.TagCommand command = Parser.parseTagCommand("tag 2 fun", Parser.tagKeywordLength(), 3);

        assertEquals(1, command.index());
        assertEquals(List.of("fun"), command.tagNames());
    }

    @Test
    void parseTagCommand_multipleTags_returnsAllTagNamesInOrder() throws BotzillaException {
        Parser.TagCommand command = Parser.parseTagCommand("tag 1 fun urgent", Parser.tagKeywordLength(), 3);

        assertEquals(List.of("fun", "urgent"), command.tagNames());
    }

    @Test
    void parseTagCommand_tagWithHashPrefix_keepsRawTokenForTaskToNormalize() throws BotzillaException {
        Parser.TagCommand command = Parser.parseTagCommand("tag 1 #fun", Parser.tagKeywordLength(), 3);

        assertEquals(List.of("#fun"), command.tagNames());
    }

    @Test
    void parseTagCommand_missingTagName_throwsException() {
        int keywordLength = Parser.tagKeywordLength();
        assertThrows(BotzillaException.class, () -> Parser.parseTagCommand("tag 1", keywordLength, 3));
    }

    @Test
    void parseTagCommand_missingIndexAndTagName_throwsException() {
        int keywordLength = Parser.tagKeywordLength();
        assertThrows(BotzillaException.class, () -> Parser.parseTagCommand("tag", keywordLength, 3));
    }

    @Test
    void parseTagCommand_nonNumericIndex_throwsException() {
        int keywordLength = Parser.tagKeywordLength();
        assertThrows(BotzillaException.class, () -> Parser.parseTagCommand("tag abc fun", keywordLength, 3));
    }

    @Test
    void parseTagCommand_indexOutOfRange_throwsException() {
        int keywordLength = Parser.tagKeywordLength();
        assertThrows(BotzillaException.class, () -> Parser.parseTagCommand("tag 99 fun", keywordLength, 3));
    }

    // ---- inline "#tag" extraction: todo ----
    @Test
    void parseTodo_noInlineTag_hasEmptyTags() throws BotzillaException {
        Task task = Parser.parseTodo("todo read book");

        assertEquals("read book", task.getName());
        assertTrue(task.getTags().isEmpty());
    }

    @Test
    void parseTodo_withInlineTag_stripsTagFromNameAndAddsItToTask() throws BotzillaException {
        Task task = Parser.parseTodo("todo read book #fun");

        assertEquals("read book", task.getName());
        assertEquals(Set.of("fun"), task.getTags());
    }

    @Test
    void parseTodo_withMultipleInlineTags_addsAllOfThem() throws BotzillaException {
        Task task = Parser.parseTodo("todo read book #fun #easy");

        assertEquals("read book", task.getName());
        assertEquals(Set.of("fun", "easy"), task.getTags());
    }

    // ---- inline "#tag" extraction: deadline ----
    @Test
    void parseDeadline_withInlineTagBeforeByClause_stripsTagAndKeepsDate() throws BotzillaException {
        Task task = Parser.parseDeadline("deadline submit report #urgent /by 2/12/2019 1800");

        assertEquals("submit report", task.getName());
        assertEquals(Set.of("urgent"), task.getTags());
        assertTrue(task.getDate().isPresent());
    }

    @Test
    void parseDeadline_withInlineTagAfterByClause_stripsTagAndKeepsDate() throws BotzillaException {
        Task task = Parser.parseDeadline("deadline submit report /by 2/12/2019 1800 #urgent");

        assertEquals("submit report", task.getName());
        assertEquals(Set.of("urgent"), task.getTags());
        assertTrue(task.getDate().isPresent());
    }

    // ---- inline "#tag" extraction: event ----
    @Test
    void parseEvent_withInlineTags_stripsTagsAndKeepsBothDates() throws BotzillaException {
        Task task = Parser.parseEvent("event party /from 1/1/2026 1800 /to 1/1/2026 2200 #fun #social");

        assertEquals("party", task.getName());
        assertEquals(Set.of("fun", "social"), task.getTags());
        assertTrue(task.getDate().isPresent());
    }
}
