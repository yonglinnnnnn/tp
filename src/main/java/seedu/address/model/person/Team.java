package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Team in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTeamName(String)}
 */
public class Team {

    public static final String MESSAGE_CONSTRAINTS =
            "Team names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * Same validation as Name.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullName;
    public final String id;

    /**
     * Constructs a {@code Team}.
     *
     * @param name A valid team name.
     */
    public Team(String name) {
        requireNonNull(name);
        checkArgument(isValidTeamName(name), MESSAGE_CONSTRAINTS);
        this.fullName = name;
        this.id = "T" + name;
    }

    /**
     * Returns true if a given string is a valid team name.
     */
    public static boolean isValidTeamName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Team otherTeam)) {
            return false;
        }
        return fullName.equals(otherTeam.fullName) && id.equals(otherTeam.id);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode() * 31 + id.hashCode();
    }
}
