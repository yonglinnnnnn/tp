package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.SetSubteamCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SetSubteamCommand object
 */
public class SetSubteamCommandParser implements Parser<SetSubteamCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SetSubteamCommand
     * and returns a SetSubteamCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SetSubteamCommand parse(String args) throws ParseException {
        try {
            requireNonNull(args);
            String trimmed = args.trim();
            if (trimmed.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        SetSubteamCommand.MESSAGE_USAGE));
            }
            String[] tokens = trimmed.split("\\s+");
            if (tokens.length != 2) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        SetSubteamCommand.MESSAGE_USAGE));
            }
            return new SetSubteamCommand(tokens[0], tokens[1]);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetSubteamCommand.MESSAGE_USAGE), pe);
        }
    }

}
