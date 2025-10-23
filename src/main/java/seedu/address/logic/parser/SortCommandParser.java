package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GITHUB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TEAM;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * Parses input arguments and creates a new SortCommand object
 */
public class SortCommandParser implements Parser<SortCommand> {
    private final static String FIELD_NAME = "name";
    private final static String FIELD_PHONE = "hp";
    private final static String FIELD_EMAIL = "em";
    private final static String FIELD_ADDRESS = "addr";
    private final static String FIELD_GITHUB = "gh";
    private final static String FIELD_ID = "id";
    private final static String FIELD_SALARY = "salary";
    private final static String FIELD_TEAM = "team";

    public static Comparator<Person> createComparator(String[] tokens) throws ParseException {
        Comparator<Person> comparator = (x, y) -> 0;
        for (String token : tokens) {
            comparator = switch (token) {
                case FIELD_NAME -> comparator.thenComparing(person -> person.name().fullName());
                case FIELD_PHONE -> comparator.thenComparing(person -> person.phone().value());
                case FIELD_EMAIL -> comparator.thenComparing(person -> person.email().value());
                case FIELD_ADDRESS -> comparator.thenComparing(person -> person.address().value());
                case FIELD_GITHUB -> comparator.thenComparing(person -> person.gitHubUsername().value());
                case FIELD_ID -> comparator.thenComparing(Person::id);
                case FIELD_SALARY -> comparator.thenComparing(person -> person.salary());
                case FIELD_TEAM -> {
                    Function<Person, String> selector = person ->
                            person.teamIds()
                                  .stream()
                                  .sorted()
                                  .reduce((x, y) -> x + y)
                                  .orElse("");
                    yield comparator.thenComparing(selector);
                }

                default -> throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
            };
        }

        return comparator;
    }

    @Override
    public SortCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String[] tokens = args.trim().split(" -");
        if (!tokens[0].equals(SortCommand.COMMAND_WORD)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        if (tokens.length == 1) {
            return new SortCommand(Comparator.comparing(person -> person.name().fullName()));
        }

        return new SortCommand(createComparator(Arrays.copyOfRange(tokens, 1, tokens.length)));
    }
}
