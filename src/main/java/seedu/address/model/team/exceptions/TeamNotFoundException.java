package seedu.address.model.team.exceptions;

/**
 * Signals that the requested team was not found.
 */
public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException() {
        super("Team not found");
    }
}
