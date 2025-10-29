package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.audit.AuditLog;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.team.Team;
import seedu.address.model.team.TeamName;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void resetData_withAuditLog_restoresAuditLog() {
        // Create an address book with audit log entries
        AddressBook sourceAddressBook = new AddressBook();
        sourceAddressBook.addPerson(ALICE);
        sourceAddressBook.addAuditEntry("ADD", "Added Alice");
        sourceAddressBook.addAuditEntry("EDIT", "Edited Alice");

        // Reset data to a new address book
        AddressBook targetAddressBook = new AddressBook();
        targetAddressBook.resetData(sourceAddressBook);

        // Verify audit log is restored
        assertEquals(2, targetAddressBook.getAuditLog().getEntries().size());
        assertEquals("ADD", targetAddressBook.getAuditLog().getEntries().get(0).getAction());
        assertEquals("Added Alice", targetAddressBook.getAuditLog().getEntries().get(0).getDetails());
        assertEquals("EDIT", targetAddressBook.getAuditLog().getEntries().get(1).getAction());
        assertEquals("Edited Alice", targetAddressBook.getAuditLog().getEntries().get(1).getDetails());
    }

    @Test
    public void resetData_withExistingAuditLog_clearsAndRestores() {
        // Create target address book with existing audit log
        AddressBook targetAddressBook = new AddressBook();
        targetAddressBook.addAuditEntry("DELETE", "Old entry");

        // Create source address book with different audit log
        AddressBook sourceAddressBook = new AddressBook();
        sourceAddressBook.addAuditEntry("ADD", "New entry");

        // Reset data
        targetAddressBook.resetData(sourceAddressBook);

        // Verify old audit log is cleared and new one is restored
        assertEquals(1, targetAddressBook.getAuditLog().getEntries().size());
        assertEquals("ADD", targetAddressBook.getAuditLog().getEntries().get(0).getAction());
        assertEquals("New entry", targetAddressBook.getAuditLog().getEntries().get(0).getDetails());
    }


    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void setNullTeam_returnsFalse() {
        Team team = new Team("T1001", new TeamName("A"));
        assertFalse(addressBook.setSubteam(null, team));
        assertFalse(addressBook.setSubteam(team, null));
        assertFalse(addressBook.setSubteam(null, null));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName()
                + "{persons=" + addressBook.getPersonList()
                + ", teams=" + addressBook.getTeamList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Team> getTeamList() {
            throw new UnsupportedOperationException("Teams not supported in this stub");
        }

        @Override
        public AuditLog getAuditLog() {
            return new AuditLog();
        }
    }
}
