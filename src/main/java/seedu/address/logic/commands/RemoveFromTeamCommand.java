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
 * Removes a person from a team.
 */
public class RemoveFromTeamCommand extends Command {

    public static final String COMMAND_WORD = "remove-from-team";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes a person from a team. "
            + "Parameters: TEAM_ID PERSON_ID\n"
            + "Example: " + COMMAND_WORD + " T0001 E0001";

    public static final String MESSAGE_TEAM_NOT_FOUND = "No team with ID %1$s found";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No person with ID %1$s found";
    public static final String MESSAGE_NOT_MEMBER = "Person %1$s is not a member of team %2$s";
    public static final String MESSAGE_SUCCESS = "Person %1$s removed from team %2$s";

    private final String teamId;
    private final String personId;

    /**
     * Creates a RemoveFromTeamCommand with a team id and person id.
     *
     * @param teamId non-null team id
     * @param personId non-null person id
     */
    public RemoveFromTeamCommand(String teamId, String personId) {
        requireNonNull(teamId);
        requireNonNull(personId);
        this.teamId = teamId;
        this.personId = personId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person person = model.find(p -> Objects.equals(p.id(), personId));
        if (person == null) {
            throw new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, personId));
        }

        Team team = getTeam(model);

        // create edited team copy with the member removed
        Team edited = new Team(team.getId(), team.getTeamName());
        List<String> newMembers = new ArrayList<>(team.getMembers());

        updateSelectedPerson(model, person, newMembers, edited);

        updateTeamDetails(model, team, newMembers, edited);
        return new CommandResult(String.format(MESSAGE_SUCCESS, personId, teamId));
    }

    private static void updateTeamDetails(Model model, Team team, List<String> newMembers, Team edited) {
        // copy leader if present and still a member -> if leader removed, leader is cleared
        if (team.getLeaderId() != null && newMembers.contains(team.getLeaderId())) {
            edited.withLeader(team.getLeaderId());
        }

        // copy subteams and parent
        edited.withSubteams(team.getSubteams());
        if (team.getParentTeamId() != null) {
            edited.withParentTeamId(team.getParentTeamId());
        }

        model.setTeam(team, edited);
    }

    private Team getTeam(Model model) throws CommandException {
        ReadOnlyAddressBook ab = model.getAddressBook();
        Team team = ab.getTeamList().stream()
                .filter(t -> Objects.equals(t.getId(), teamId))
                .findFirst()
                .orElse(null);
        if (team == null) {
            throw new CommandException(String.format(MESSAGE_TEAM_NOT_FOUND, teamId));
        }

        if (!team.getMembers().contains(personId)) {
            throw new CommandException(String.format(MESSAGE_NOT_MEMBER, personId, teamId));
        }
        return team;
    }

    private void updateSelectedPerson(Model model, Person person, List<String> newMembers, Team edited)
            throws CommandException {
        try {
            Person editedPerson = person.withRemovedTeam(teamId);
            model.setPerson(person, editedPerson);
            newMembers.remove(personId);
            edited.withMembers(newMembers);
        } catch (DuplicatePersonException e) {
            throw new CommandException("Failed to update person's teams: would create a duplicate person");
        } catch (PersonNotFoundException e) {
            throw new CommandException("Failed to update person's teams: person no longer exists");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RemoveFromTeamCommand)) {
            return false;
        }
        RemoveFromTeamCommand otherTeam = (RemoveFromTeamCommand) other;
        return teamId.equals(otherTeam.teamId) && personId.equals(otherTeam.personId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("teamId", teamId)
                .add("personId", personId)
                .toString();
    }
}
