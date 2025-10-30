package seedu.address.model.team;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Team's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTeamName(String)}
 */
public record TeamName(String teamName) {

    public static final String MESSAGE_CONSTRAINTS =
            "TeamNames must be 1 to 40 alphanumeric characters and must not contain spaces";

    /*
     * The first character of the name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^[\\p{Alnum}]{1,40}$";

    /**
     * Constructs a {@code Name}.
     *
     * @param teamName A valid name.
     */
    public TeamName {
        requireNonNull(teamName);
        checkArgument(isValidTeamName(teamName), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidTeamName(String test) {
        return test.matches(VALIDATION_REGEX);
    }
}
