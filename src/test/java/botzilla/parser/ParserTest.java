package botzilla.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
}
