package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.team.Team;

/**
 * Deletes a team identified by its team ID.
 * Also removes the team id from all member Person objects and removes the team
 * from any parent's subteams by scanning all teams in the address book.
 */
public class DeleteTeamCommand extends Command {

    public static final String COMMAND_WORD = "delete-team";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the team identified by the team ID.\n"
            + "Parameters: team ID (must be in format Txxxx)\n"
            + "Example: " + COMMAND_WORD + " T0001";

    public static final String MESSAGE_TEAM_NOT_FOUND = "No team found with team ID: %1$s";
    public static final String MESSAGE_HAS_SUBTEAMS = "Cannot delete team %1$s because it has subteams";
    public static final String MESSAGE_DELETE_TEAM_SUCCESS = "Deleted Team: %1$s";
    public static final String MESSAGE_FAILED_UPDATE_PERSON = "Failed to update person when removing team: %1$s";

    private final String teamId;

    /**
     * Creates a {@code DeleteTeamCommand} for deleting the specified team.
     *
     * @param teamId the ID of the team to delete; must not be null and must follow the team ID format.
     */
    public DeleteTeamCommand(String teamId) {
        requireNonNull(teamId);
        this.teamId = teamId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ReadOnlyAddressBook ab = model.getAddressBook();
        Team teamToDelete = ab.getTeamList().stream()
                .filter(t -> Objects.equals(t.getId(), teamId))
                .findFirst()
                .orElse(null);

        if (teamToDelete == null) {
            throw new CommandException(String.format(MESSAGE_TEAM_NOT_FOUND, teamId));
        }

        // Block deletion if team has subteams
        if (!teamToDelete.getSubteams().isEmpty()) {
            throw new CommandException(String.format(MESSAGE_HAS_SUBTEAMS, teamId));
        }

        // Remove team id from member Person objects
        updatePersonDetails(model, teamToDelete);

        // Remove references to this team from any team's subteams by scanning all teams
        for (Team potentialParent : ab.getTeamList()) {
            Team editedParent = getEditedParentDetails(potentialParent);
            if (editedParent == null) {
                continue;
            }
            model.setTeam(potentialParent, editedParent);
        }

        // Remove the team from the model
        model.removeTeam(teamToDelete);

        return new CommandResult(String.format(MESSAGE_DELETE_TEAM_SUCCESS, teamId));
    }

    private void updatePersonDetails(Model model, Team teamToDelete) throws CommandException {
        List<String> memberIds = new ArrayList<>(teamToDelete.getMembers());
        for (String memberId : memberIds) {
            Person person = model.find(p -> Objects.equals(p.id(), memberId));
            if (person == null) {
                continue;
            }
            try {
                Person editedPerson = person.withRemovedTeam(teamId);
                model.setPerson(person, editedPerson);
            } catch (DuplicatePersonException e) {
                throw new CommandException(String.format(MESSAGE_FAILED_UPDATE_PERSON, memberId));
            } catch (PersonNotFoundException e) {
                throw new CommandException(String.format(MESSAGE_FAILED_UPDATE_PERSON, memberId));
            }
        }
    }

    /**
     * Returns a copy of {@code potentialParent} with the subteam matching {@code teamId} removed,
     * preserving members, remaining subteams, the leader (only if still a member), and the parent reference.
     * Returns {@code null} if {@code potentialParent} does not contain the team. Input is not mutated.
     *
     * @param potentialParent non-null team to inspect
     * @return edited copy with the subteam removed, or {@code null}
     */
    private Team getEditedParentDetails(Team potentialParent) {
        boolean contains = potentialParent.getSubteams().stream()
                .anyMatch(st -> Objects.equals(st.getId(), teamId));
        if (!contains) {
            return null;
        }

        Team editedParent = new Team(potentialParent.getId(), potentialParent.getTeamName());

        // copy members
        editedParent.withMembers(new ArrayList<>(potentialParent.getMembers()));

        // copy subteams excluding the deleted team
        List<Team> newSubteams = new ArrayList<>(potentialParent.getSubteams());
        newSubteams.removeIf(st -> Objects.equals(st.getId(), teamId));
        editedParent.withSubteams(newSubteams);

        // preserve leader if still valid
        if (potentialParent.getLeaderId() != null
                && editedParent.getMembers().contains(potentialParent.getLeaderId())) {
            editedParent.withLeader(potentialParent.getLeaderId());
        }

        // preserve parent's parent if present
        if (potentialParent.getParentTeam() != null) {
            editedParent.withParentTeam(potentialParent.getParentTeam());
        }

        return editedParent;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteTeamCommand)) {
            return false;
        }
        DeleteTeamCommand otherCmd = (DeleteTeamCommand) other;
        return teamId.equals(otherCmd.teamId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("teamId", teamId)
                .toString();
    }
}
