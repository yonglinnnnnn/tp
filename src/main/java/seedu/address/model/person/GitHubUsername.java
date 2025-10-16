package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's GitHub username.
 * Guarantees: immutable; is valid as declared in {@link #isValidGitHubUsername(String)}
 */
public record GitHubUsername(String value) {

    public static final String MESSAGE_CONSTRAINTS = """
                GitHub username should begin with '@', only contain alphanumeric characters and hyphens,
                start with an alphanumeric character, cannot have multiple consecutive hyphens,
                cannot begin or end with a hyphen, and must be 1-39 chars.""";

    public static final String VALIDATION_REGEX = "^@[a-zA-Z0-9](?:[a-zA-Z0-9]|-(?=[a-zA-Z0-9])){2,38}$";

    /**
     * Constructs a {@code GitHubUserName}.
     *
     * @param value A valid username.
     */
    public GitHubUsername {
        requireNonNull(value);
        checkArgument(isValidGitHubUsername(value), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid GitHub username.
     */
    public static boolean isValidGitHubUsername(String test) {
        return test.matches(VALIDATION_REGEX);
    }
}
