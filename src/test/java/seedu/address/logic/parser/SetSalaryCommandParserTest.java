package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.SetSalaryCommand;

class SetSalaryCommandParserTest {
    private SetSalaryCommandParser parser = new SetSalaryCommandParser();

    @Test
    void parse_validFormat_success() {
        assertParseSuccess(parser, "E12345 100", new SetSalaryCommand("E12345", 100));
    }

    @Test
    void parse_invalidFormat_failure() {
        assertParseFailure(parser, "E12345",
                           String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetSalaryCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "E12345,100",
                           String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetSalaryCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_invalidId_failure() {
        assertParseFailure(parser, "12345 100", Messages.MESSAGE_INVALID_PERSON_ID);
    }

    @Test
    void parse_invalidSalary_failure() {
        assertParseFailure(parser, "E12345 -100", Messages.MESSAGE_INVALID_SALARY);
    }
}