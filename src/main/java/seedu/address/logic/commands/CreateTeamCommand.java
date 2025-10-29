package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.team.Team;
import seedu.address.model.team.TeamName;

/**
 * Creates a team in the address book.
 */
public class CreateTeamCommand extends Command {

    public static final String COMMAND_WORD = "create-team";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a team. "
            + "Parameters: NAME leaderPersonId\n"
            + "Example: " + COMMAND_WORD + " Team Alpha E0001";

    public static final String MESSAGE_SUCCESS = "New team created: %1$s";
    public static final String MESSAGE_DUPLICATE_TEAM = "This team already exists in the address book";
    public static final String MESSAGE_LEADER_NOT_FOUND = "No person with ID %1$s found";

    /**
     * Static id generator starting at 1 so first id is T0001.
     */
    private static long nextId = 1;

    private final String teamName;
    private final String leaderPersonId;

    /**
     * Creates a CreateTeamCommand that will create a new team with the given name
     * and set the given person id as the team's leader.
     *
     * @param teamName        non-null, validated team name
     * @param leaderPersonId  non-null id of the leader person
     */
    public CreateTeamCommand(String teamName, String leaderPersonId) {
        requireNonNull(teamName);
        requireNonNull(leaderPersonId);
        this.teamName = teamName;
        this.leaderPersonId = leaderPersonId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        final TeamName validatedName;
        try {
            validatedName = new TeamName(teamName);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }

        List<Person> persons = model.getFilteredPersonList();
        Optional<Person> leaderOpt = persons.stream()
                .filter(p -> leaderPersonId.equals(p.id()))
                .findFirst();
        if (leaderOpt.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_LEADER_NOT_FOUND, leaderPersonId));
        }

        String id = String.format("T%04d", nextId++);
        Team toAdd = new Team(id, validatedName);

        Person leader = leaderOpt.get();
        toAdd.withLeader(leader.id());
        updateLeaderPersonDetails(model, leaderOpt, id);


        if (model.hasTeam(toAdd)) {
            // revert id increment
            nextId--;
            throw new CommandException(MESSAGE_DUPLICATE_TEAM);
        }

        ReadOnlyAddressBook ab = model.getAddressBook();
        boolean nameDuplicate = ab.getTeamList().stream()
                .anyMatch(t -> t.getTeamName().equals(toAdd.getTeamName()));
        if (nameDuplicate) {
            // revert id increment
            nextId--;
            throw new CommandException(MESSAGE_DUPLICATE_TEAM);
        }

        model.addTeam(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.getTeamName().teamName()));
    }

    private static void updateLeaderPersonDetails(Model model, Optional<Person> leaderOpt, String id)
            throws CommandException {
        try {
            Person editedPerson = leaderOpt.get().withAddedTeam(id);
            model.setPerson(leaderOpt.get(), editedPerson);
        } catch (DuplicatePersonException e) {
            throw new CommandException("Failed to update Leader's teams: would create a duplicate leader");
        } catch (PersonNotFoundException e) {
            throw new CommandException("Failed to update Leader's teams: leader no longer exists");
        }
    }

    /**
     * Sets the next ID based on the highest existing ID in the team list.
     * Should be called when the application starts.
     */
    public static void setNextId(long id) {
        nextId = id;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CreateTeamCommand)) {
            return false;
        }
        CreateTeamCommand otherCmd = (CreateTeamCommand) other;
        return teamName.equals(otherCmd.teamName)
                && leaderPersonId.equals(otherCmd.leaderPersonId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("teamName", teamName)
                .add("leaderPersonId", leaderPersonId)
                .toString();
    }
}
