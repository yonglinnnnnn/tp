package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Pattern;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    private static final Pattern EMPLOYEE_ID_FORMAT = Pattern.compile("^E\\d{4}$");

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * The employee ID must be in the format Exxxx where x represents a digit (0-9).
     *
     * @param args the employee ID string to parse (e.g., E1234)
     * @return a DeleteCommand object with the parsed employee ID
     * @throws ParseException if the employee ID does not match the format Exxxx
     */

    @Override
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (!EMPLOYEE_ID_FORMAT.matcher(trimmedArgs).matches()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(trimmedArgs);
    }

}
