package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteTeamCommand;

/**
 * Tests for {@link DeleteTeamCommandParser}.
 */
public class DeleteTeamCommandParserTest {

    private final DeleteTeamCommandParser parser = new DeleteTeamCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteTeamCommand() {
        assertParseSuccess(parser, "T0001", new DeleteTeamCommand("T0001"));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // not in Txxxx format
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTeamCommand.MESSAGE_USAGE));

        // missing T prefix
        assertParseFailure(parser, "0001",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTeamCommand.MESSAGE_USAGE));

        // wrong number of digits
        assertParseFailure(parser, "T123",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTeamCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "T12345",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTeamCommand.MESSAGE_USAGE));

        // lowercase t
        assertParseFailure(parser, "t1234",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTeamCommand.MESSAGE_USAGE));
    }
}
