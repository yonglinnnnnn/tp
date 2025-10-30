package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.audit.AuditLog;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.team.Team;
import seedu.address.model.team.UniqueTeamList;
import seedu.address.model.team.exceptions.TeamNotFoundException;

/**
 * Represents an in-memory address book containing persons and teams.
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons = new UniquePersonList();
    private final UniqueTeamList teams = new UniqueTeamList();
    private final AuditLog auditLog = new AuditLog();


    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons and Teams in the {@code toBeCopied}.
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the persons list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the teams list with {@code teams}.
     * {@code teams} must not contain duplicate teams.
     */
    public void setTeams(List<Team> teams) {
        this.teams.setTeams(teams);
    }

    /**
     * Replaces this address book's data with the provided {@code newData}.
     * Persons are always replaced. Teams are replaced only if {@code newData}
     * exposes a team list; otherwise team data is left unchanged. (to be cleaned further later)
     *
     * @param newData the source data to copy; must not be null
     * @throws NullPointerException if {@code newData} is null
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());

        // Only restore audit log if it's not empty in the new data
        // This prevents clearing the audit log when clearing persons/teams
        if (!newData.getAuditLog().getEntries().isEmpty()) {
            auditLog.clear();
            for (var entry : newData.getAuditLog().getEntries()) {
                auditLog.addEntry(entry.getAction(), entry.getDetails(), entry.getTimestamp());
            }
        }

        // ReadOnlyAddressBook is expected to expose getTeamList()
        if (newData instanceof ReadOnlyAddressBook) {
            try {
                setTeams(((ReadOnlyAddressBook) newData).getTeamList());
            } catch (UnsupportedOperationException | ClassCastException e) {
                // If the provided ReadOnlyAddressBook does not expose teams yet, ignore.
            }
        }
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    @Override
    public AuditLog getAuditLog() {
        return auditLog;
    }

    public void addAuditEntry(String action, String details) {
        auditLog.addEntry(action, details, LocalDateTime.now());
    }

    //// team-level operations

    /**
     * Returns true if a team with the same identity as {@code team} exists in the address book.
     */
    public boolean hasTeam(Team team) {
        requireNonNull(team);
        return teams.contains(team);
    }

    /**
     * Finds and returns a team by its ID.
     * Returns null if no such team exists.
     */
    public Team getTeamById(String teamId) {
        requireNonNull(teamId);
        try {
            return teams.getTeamById(teamId);
        } catch (TeamNotFoundException e) {
            return null;
        }
    }

    /**
     * Adds a team to the address book.
     * The team must not already exist in the address book.
     */
    public void addTeam(Team team) {
        requireNonNull(team);
        teams.add(team);
    }

    /**
     * Replaces the given team {@code target} in the list with {@code editedTeam}.
     * {@code target} must exist in the address book.
     */
    public void setTeam(Team target, Team editedTeam) {
        teams.setTeam(target, editedTeam);
    }

    /**
     * Removes {@code toRemove} from this {@code AddressBook}.
     * {@code toRemove} must exist in the address book.
     */
    public void removeTeam(Team toRemove) {
        teams.remove(toRemove);
    }

    /**
     * Sets {@code subteamId} as a subteam of {@code parentTeamId}.
     * Both teams must exist in the address book.
     *
     * @return true if the subteam was set successfully, false otherwise
     */
    public boolean setSubteam(Team parentTeam, Team subteam) {
        if (parentTeam == null || subteam == null) {
            return false;
        }
        return teams.setSubteam(parentTeam, subteam);
    }

    /**
     * Sorts the list of persons according to the given comparator.
     *
     * @param comparator The comparator used to compare the selected keys.
     */
    public void sortPersons(Comparator<Person> comparator) {
        requireNonNull(comparator);
        persons.sort(comparator);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("teams", teams)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    /**
     * Returns an unmodifiable view of the teams list.
     */
    public ObservableList<Team> getTeamList() {
        return teams.asUnmodifiableObservableList();
    }

    /**
     * Returns the organization hierarchy string in a Linux tree format.
     */
    public String getOrganizationHierarchyString() {
        return teams.getHierarchyString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook otherAddressBook)) {
            return false;
        }

        AddressBook otherAb = (AddressBook) other;
        return persons.equals(otherAb.persons) && teams.equals(otherAb.teams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, teams);
    }
}
