package seedu.address.logic.parser;

import seedu.address.logic.commands.SetSubteamCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetSubteamCommandParserTest {
    @Test
    void parse_validArgs_returnsSetSubteamCommand() {
        try {
            SetSubteamCommandParser parser = new SetSubteamCommandParser();
            SetSubteamCommand command = parser.parse("T0001 T0002");
            assertEquals(new SetSubteamCommand("T0001", "T0002"), command);
        } catch (ParseException e) {
            throw new AssertionError("Execution of valid SetSubteamCommand should not fail.", e);
        }
    }

    @Test
    void parse_missingArgs_throwsParseException() {
        SetSubteamCommandParser parser = new SetSubteamCommandParser();
        assertThrows(ParseException.class, () -> parser.parse("T0001"));
    }

    @Test
    void parse_extraArgs_throwsParseException() {
        SetSubteamCommandParser parser = new SetSubteamCommandParser();
        assertThrows(ParseException.class, () -> parser.parse("T0001 T0002 T0003"));
    }

    @Test
    void parse_emptyArgs_throwsParseException() {
        SetSubteamCommandParser parser = new SetSubteamCommandParser();
        assertThrows(ParseException.class, () -> parser.parse(""));
    }

    @Test
    void parse_nullArgs_throwsNullPointerException() {
        SetSubteamCommandParser parser = new SetSubteamCommandParser();
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }
}
