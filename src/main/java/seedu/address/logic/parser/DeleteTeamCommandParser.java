package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Pattern;

import seedu.address.logic.commands.DeleteTeamCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteTeamCommand object
 */
public class DeleteTeamCommandParser implements Parser<DeleteTeamCommand> {

    private static final Pattern TEAM_ID_FORMAT = Pattern.compile("^T\\d{4}$");

    @Override
    public DeleteTeamCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (!TEAM_ID_FORMAT.matcher(trimmedArgs).matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTeamCommand.MESSAGE_USAGE));
        }

        return new DeleteTeamCommand(trimmedArgs);
    }
}
