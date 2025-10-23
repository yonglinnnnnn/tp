package seedu.address.model;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.audit.AuditLog;
import seedu.address.model.person.Person;
import seedu.address.model.team.Team;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);


    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);


    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    Person find(java.util.function.Predicate<Person> predicate);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns the organization hierarchy in Linux tree format.
     */
    String getOrganizationHierarchyString();

    /**
     * Adds an entry to the audit log.
     */
    void addAuditEntry(String action, String details);

    /**
     * Returns the audit log.
     */
    AuditLog getAuditLog();

    /**
     * Returns true if a team with the same identity as {@code team} exists in the address book.
     */
    boolean hasTeam(Team team);

    /**
     * Adds a team to the address book.
     */
    void addTeam(Team team);

    /**
     * Replaces the given team {@code target} in the address book with {@code editedTeam}.
     */
    void setTeam(Team target, Team editedTeam);

    /**
     * Removes the given team from the address book.
     */
    void removeTeam(Team team);

    /**
     * Sorts the list of persons according to the given selector and comparator.
     * @param comparator The comparator used to compare the selected keys.
     */
    void sortPersons(Comparator<Person> comparator);
}
