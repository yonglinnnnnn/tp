package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.SetSalaryCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SetSalaryCommand object
 */
public class SetSalaryCommandParser implements Parser<SetSalaryCommand> {
    private static final Pattern NUMBER_REGEX_PATTERN = Pattern.compile("^(\\d+)(?:\\.(\\d+))?$");

    private static int parseDecimalPart(String string) {
        if (string == null) {
            return 0;
        }

        return (int) (Double.parseDouble(String.format("0.%s", string)) * 100);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the SetSalaryCommand
     * and returns a SetSalaryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SetSalaryCommand parse(String args) throws ParseException {
        String[] tokens = args.trim().split("\\s+");
        if (tokens.length < 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetSalaryCommand.MESSAGE_USAGE));
        }

        String id = tokens[0];
        if (!id.startsWith("E")) {
            throw new ParseException(Messages.MESSAGE_INVALID_PERSON_ID);
        }

        Matcher matcher = NUMBER_REGEX_PATTERN.matcher(tokens[1]);

        if (matcher.matches()) {
            int integerPart = Integer.parseInt(matcher.group(1));
            int decimalPart = parseDecimalPart(matcher.group(2));
            if (integerPart < 0 || decimalPart < 0) {
                throw new ParseException(Messages.MESSAGE_INVALID_SALARY);
            }

            return new SetSalaryCommand(tokens[0], integerPart * 100 + decimalPart);
        }

        throw new ParseException(Messages.MESSAGE_INVALID_SALARY);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
