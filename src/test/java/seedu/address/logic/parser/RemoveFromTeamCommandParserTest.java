package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.RemoveFromTeamCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Tests for {@link RemoveFromTeamCommandParser}.
 */
public class RemoveFromTeamCommandParserTest {

    private final RemoveFromTeamCommandParser parser = new RemoveFromTeamCommandParser();

    @Test
    public void parse_validArgs_returnsCommand() throws Exception {
        RemoveFromTeamCommand cmd = parser.parse("T0001 E0001");
        assertEquals(new RemoveFromTeamCommand("T0001", "E0001"), cmd);
    }

    @Test
    public void parse_tooFewArgs_throwsParseException() {
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse("T0001"));
        assertEquals(String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                RemoveFromTeamCommand.MESSAGE_USAGE), ex.getMessage());
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse("   "));
        assertEquals(String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                RemoveFromTeamCommand.MESSAGE_USAGE), ex.getMessage());
    }

    @Test
    public void equals_identityAndNull() {
        RemoveFromTeamCommandParser parser = new RemoveFromTeamCommandParser();
        assertTrue(parser.equals(parser));
        assertFalse(parser.equals(null));
    }

    @Test
    public void equals_differentInstancesNotEqual() {
        RemoveFromTeamCommandParser p1 = new RemoveFromTeamCommandParser();
        RemoveFromTeamCommandParser p2 = new RemoveFromTeamCommandParser();
        assertFalse(p1.equals(p2));
    }

    @Test
    public void toString_containsClassName() {
        RemoveFromTeamCommandParser parser = new RemoveFromTeamCommandParser();
        String s = parser.toString();
        assertTrue(s.contains("RemoveFromTeamCommandParser"));
    }
}
