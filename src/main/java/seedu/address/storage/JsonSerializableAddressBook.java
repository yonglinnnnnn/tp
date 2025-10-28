package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.audit.AuditLogEntry;
import seedu.address.model.person.Person;
import seedu.address.model.team.Team;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedTeam> teams = new ArrayList<>();

    @JsonProperty("auditLog")
    private final List<JsonAdaptedAuditLogEntry> auditLogEntries = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                       @JsonProperty("teams") List<JsonAdaptedTeam> teams,
                                       @JsonProperty("auditLog") List<JsonAdaptedAuditLogEntry> auditLogEntries) {
        this.persons.addAll(persons);
        if (teams != null) {
            this.teams.addAll(teams);
        }
        if (auditLogEntries != null) {
            this.auditLogEntries.addAll(auditLogEntries);
        }
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).toList());
        teams.addAll(source.getTeamList().stream().map(JsonAdaptedTeam::new).toList());
        auditLogEntries.addAll(source.getAuditLog().getEntries().stream()
                .map(JsonAdaptedAuditLogEntry::new).toList());
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            addressBook.addPerson(person);
        }
        for (JsonAdaptedTeam jsonAdaptedTeam : teams) {
            Team team = jsonAdaptedTeam.toModelType();
            addressBook.addTeam(team);
        }
        for (JsonAdaptedAuditLogEntry jsonAdaptedEntry : auditLogEntries) {
            AuditLogEntry entry = jsonAdaptedEntry.toModelType();
            addressBook.getAuditLog().addEntry(entry.getAction(), entry.getDetails(), entry.getTimestamp());
        }
        return addressBook;
    }

}
