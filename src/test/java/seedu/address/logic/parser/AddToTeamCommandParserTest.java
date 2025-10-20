package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddToTeamCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Tests for {@link AddToTeamCommandParser}.
 */
public class AddToTeamCommandParserTest {

    private final AddToTeamCommandParser parser = new AddToTeamCommandParser();

    @Test
    public void parse_validArgs_returnsCommand() throws Exception {
        AddToTeamCommand cmd = parser.parse("T0001 E0001");
        assertEquals(new AddToTeamCommand("T0001", "E0001"), cmd);
    }

    @Test
    public void parse_tooFewArgs_throwsParseException() {
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse("T0001"));
        assertEquals(String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                AddToTeamCommand.MESSAGE_USAGE), ex.getMessage());
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse("   "));
        assertEquals(String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                AddToTeamCommand.MESSAGE_USAGE), ex.getMessage());
    }

    @Test
    public void equals_identityAndNull() {
        AddToTeamCommandParser parser = new AddToTeamCommandParser();
        assertTrue(parser.equals(parser));
        assertFalse(parser.equals(null));
    }

    @Test
    public void equals_differentInstancesNotEqual() {
        AddToTeamCommandParser p1 = new AddToTeamCommandParser();
        AddToTeamCommandParser p2 = new AddToTeamCommandParser();
        assertFalse(p1.equals(p2));
    }

    @Test
    public void toString_containsClassName() {
        AddToTeamCommandParser parser = new AddToTeamCommandParser();
        String s = parser.toString();
        assertTrue(s.contains("AddToTeamCommandParser"));
    }
}
