package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.Set;

import seedu.address.logic.commands.UntagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new UntagCommand object
 */
public class UntagCommandParser implements Parser<UntagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UntagCommand
     * and returns an UntagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UntagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmed.split("\\s+", 2); // Split into employee ID and rest

        if (tokens.length < 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
        }

        String employeeId = tokens[0];

        // Validate employee ID format (must start with 'E')
        if (!employeeId.startsWith("E")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UntagCommand.MESSAGE_USAGE));
        }

        // Parse tags from the remaining tokens
        String[] tagTokens = tokens[1].split("\\s+");
        Set<Tag> tagList = ParserUtil.parseTags(Arrays.asList(tagTokens));

        if (tagList.isEmpty()) {
            throw new ParseException(UntagCommand.MESSAGE_NO_TAGS_PROVIDED);
        }

        return new UntagCommand(employeeId, tagList);
    }
}
