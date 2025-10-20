package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.team.Team;
import seedu.address.model.team.UniqueTeamList;

/**
 * Represents an in-memory address book containing persons and teams.
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons = new UniquePersonList();
    private final UniqueTeamList teams = new UniqueTeamList();

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
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
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

    //// team-level operations

    /**
     * Returns true if a team with the same identity as {@code team} exists in the address book.
     */
    public boolean hasTeam(Team team) {
        requireNonNull(team);
        return teams.contains(team);
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

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddressBook)) {
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
