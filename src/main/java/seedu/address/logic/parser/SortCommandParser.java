package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * Parses input arguments and creates a new SortCommand object
 */
public class SortCommandParser implements Parser<SortCommand> {
    private static final String FIELD_NAME = "name";
    private static final String FIELD_PHONE = "hp";
    private static final String FIELD_EMAIL = "em";
    private static final String FIELD_ADDRESS = "addr";
    private static final String FIELD_GITHUB = "gh";
    private static final String FIELD_ID = "id";
    private static final String FIELD_SALARY = "salary";
    private static final String FIELD_TEAM = "team";

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

            default -> throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                              SortCommand.MESSAGE_USAGE));
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
