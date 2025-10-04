package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's GitHub username.
 * Guarantees: immutable; is valid as declared in {@link #isValidGitHubUserName(String)}
 */
public record GitHubUsername(String value) {

    public static final String MESSAGE_CONSTRAINTS =
            "GitHub username should only contain alphanumeric characters and hyphens, start with an alphanumeric,\n"
                    + "cannot have multiple consecutive hyphens, "
                    + "cannot begin or end with a hyphen, and must be 1-39 chars.";

    /*
     * Using the same validation approach as Name for simplicity in this task.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    /**
     * Constructs a {@code GitHubUserName}.
     *
     * @param value A valid username.
     */
    public GitHubUsername {
        requireNonNull(value);
        checkArgument(isValidGitHubUserName(value), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid GitHub username.
     */
    public static boolean isValidGitHubUserName(String test) {
        return test.matches(VALIDATION_REGEX);
    }
}
