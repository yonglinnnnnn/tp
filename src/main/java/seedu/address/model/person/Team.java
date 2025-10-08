package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Team in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTeamName(String)}
 */
public record Team(String name, String id) {

    public static final String MESSAGE_CONSTRAINTS =
            "Team names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * Same validation as Name.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    /**
     * Constructs a {@code Team}.
     *
     * @param name A valid team name.
     * @param id A valid team id.
     */
    public Team {
        requireNonNull(name);
        checkArgument(isValidTeamName(name), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid team name.
     */
    public static boolean isValidTeamName(String test) {
        return test.matches(VALIDATION_REGEX);
    }
}
