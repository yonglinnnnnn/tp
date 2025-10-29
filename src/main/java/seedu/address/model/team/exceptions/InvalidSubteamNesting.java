package seedu.address.model.team.exceptions;

/**
 * Signals that there is invalid nesting of subteams that would result in cycles.
 */
public class InvalidSubteamNesting extends RuntimeException {
    public InvalidSubteamNesting() {
        super("Invalid subteam nesting detected.");
    }
}
