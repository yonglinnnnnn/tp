package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CreateTeamCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Tests for {@link CreateTeamCommandParser}.
 */
public class CreateTeamCommandParserTest {

    private final CreateTeamCommandParser parser = new CreateTeamCommandParser();

    @Test
    public void parse_validArgs_returnsCommand() throws Exception {
        CreateTeamCommand cmd = parser.parse("Team Alpha E0001");
        assertEquals(new CreateTeamCommand("Team Alpha", "E0001"), cmd);
    }

    @Test
    public void parse_missingLeader_throwsParseException() {
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse("TeamAlphaOnly"));
        assertEquals(String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                CreateTeamCommand.MESSAGE_USAGE), ex.getMessage());
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse("   "));
        assertEquals(String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                CreateTeamCommand.MESSAGE_USAGE), ex.getMessage());
    }

    @Test
    public void equals_identityAndNull() {
        CreateTeamCommandParser parser = new CreateTeamCommandParser();
        assertTrue(parser.equals(parser));
        assertFalse(parser.equals(null));
    }

    @Test
    public void equals_differentInstancesNotEqual() {
        CreateTeamCommandParser p1 = new CreateTeamCommandParser();
        CreateTeamCommandParser p2 = new CreateTeamCommandParser();
        // parsers do not override equals; different instances are not equal
        assertFalse(p1.equals(p2));
    }

    @Test
    public void toString_containsClassName() {
        CreateTeamCommandParser parser = new CreateTeamCommandParser();
        String s = parser.toString();
        assertTrue(s.contains("CreateTeamCommandParser"));
    }
}
