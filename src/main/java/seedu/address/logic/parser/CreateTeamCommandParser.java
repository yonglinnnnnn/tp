package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.CreateTeamCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new CreateTeamCommand object.
 *
 * Expected format: {@code <TEAM_NAME> leaderPersonId}
 */
public class CreateTeamCommandParser implements Parser<CreateTeamCommand> {

    @Override
    public CreateTeamCommand parse(String args) throws ParseException {
        if (args == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateTeamCommand.MESSAGE_USAGE));
        }

        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateTeamCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmed.split("\\s+");
        if (tokens.length < 2) {
            // need at least a name and a leader id
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateTeamCommand.MESSAGE_USAGE));
        }

        String leaderPersonId = tokens[tokens.length - 1];
        String teamName = String.join(" ", Arrays.copyOf(tokens, tokens.length - 1)).trim();

        if (teamName.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateTeamCommand.MESSAGE_USAGE));
        }

        return new CreateTeamCommand(teamName, leaderPersonId);
    }
}
