package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.AddToTeamCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddToTeamCommand object.
 *
 * Expected format: {@code TEAM_ID PERSON_ID}
 */
public class AddToTeamCommandParser implements Parser<AddToTeamCommand> {

    @Override
    public AddToTeamCommand parse(String args) throws ParseException {
        if (args == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddToTeamCommand.MESSAGE_USAGE));
        }
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddToTeamCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmed.split("\\s+");
        if (tokens.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddToTeamCommand.MESSAGE_USAGE));
        }

        String teamId = tokens[0];
        String personId = tokens[1];
        return new AddToTeamCommand(teamId, personId);
    }
}
