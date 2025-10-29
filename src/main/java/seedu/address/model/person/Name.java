package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public record Name(String fullName) {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain letters and spaces, and it should be 1-50 characters long.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]{0,49}";

    /**
     * Constructs a {@code Name}.
     *
     * @param fullName A valid name.
     */
    public Name {
        requireNonNull(fullName);
        checkArgument(isValidName(fullName), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }
}
