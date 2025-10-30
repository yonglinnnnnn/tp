package seedu.address.logic;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_TAG_UNALLOWED = "Use the tag/untag command to add/remove tags";
    public static final String MESSAGE_INVALID_PERSON_ID = "Invalid person ID. ID must start with 'E'.";
    public static final String MESSAGE_INVALID_SALARY = "Invalid salary. Salary must be a positive number.";
    public static final String MESSAGE_SALARY_TOO_HIGH = "The salary value has exceeded the limit for "
            + "floating point numbers.";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Person with employee ID %1$s not found.";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + duplicateFields.stream().sorted().collect(Collectors.joining(" "));
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append("\t- Name: ").append(person.name().fullName()).append("\n")
                .append("\t- Phone: ").append(person.phone().value()).append("\n")
                .append("\t- Email: ").append(person.email().value()).append("\n")
                .append("\t- Address: ").append(person.address().value()).append("\n");

        if (!Objects.equals(person.gitHubUsername().value(), "")) {
            builder.append("\t- GitHub Username: ").append(person.gitHubUsername().value()).append("\n");
        }
        if (person.salary().toDouble() != 0.0) {
            builder.append("\t- Salary: ").append(String.valueOf(person.salary().toDouble())).append("\n");
        }
        if (!person.tags().isEmpty()) {
            builder.append("\t- Tags: ");
            person.tags().forEach(tagName -> builder.append(tagName).append(" "));
            builder.append("\n");
        }
        return builder.toString();
    }

}
