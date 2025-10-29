package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Adds a team to a subteam of an existing team.
 */
public class SetSubteamCommand extends Command {

    public static final String COMMAND_WORD = "set-subteam";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a child team as a subteam to a parent team. "
            + "Parameters: PARENT_TEAM_ID SUBTEAM_ID\n"
            + "Example: " + COMMAND_WORD + " T0001 T0002";

    public static final String MESSAGE_TEAM_NOT_FOUND = "No team with ID %1$s found";
    public static final String MESSAGE_INVALID_SUBTEAM = "Team %2$s cannot be a subteam of team %1$s";
    public static final String MESSAGE_SUCCESS = "Team %2$s added as a subteam to team %1$s";
    private static final Logger modelLogger = LogsCenter.getLogger(ModelManager.class);

    private final String parentTeamId;
    private final String subteamId;

    /**
     * Creates an AddToSubteamCommand which, when executed, adds the specified subteam to the specified parent team.
     *
     * @param parentTeamId  non-null id of the team to add the subteam to
     * @param subteamId non-null id of the subteam to be added
     */
    public SetSubteamCommand(String parentTeamId, String subteamId) {
        requireNonNull(parentTeamId);
        requireNonNull(subteamId);
        this.parentTeamId = parentTeamId;
        this.subteamId = subteamId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.hasTeamWithId(parentTeamId) || !model.hasTeamWithId(subteamId)) {
            throw new CommandException(String.format(MESSAGE_TEAM_NOT_FOUND,
                    !model.hasTeamWithId(parentTeamId) ? parentTeamId : subteamId));
        }
        boolean isValidOperation = model.setSubteam(parentTeamId, subteamId);
        if (!isValidOperation) {
            throw new CommandException(String.format(MESSAGE_INVALID_SUBTEAM, parentTeamId, subteamId));
        }
        modelLogger.info(String.format("Set team %s as subteam of team %s", subteamId, parentTeamId));
        return new CommandResult(String.format(MESSAGE_SUCCESS, parentTeamId, subteamId));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SetSubteamCommand otherAddSubteamCommand)) {
            return false;
        }
        return parentTeamId.equals(otherAddSubteamCommand.parentTeamId)
                && subteamId.equals(otherAddSubteamCommand.subteamId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("parentTeamId", parentTeamId)
                .add("subteamId", subteamId)
                .toString();
    }
}
