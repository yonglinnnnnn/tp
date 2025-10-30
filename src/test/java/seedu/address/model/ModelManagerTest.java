package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.audit.AuditLog;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.team.Team;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TeamBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void hasTeam_nullTeam_throwsNullPointerException() {
        ModelManager model = new ModelManager();
        Assertions.assertThrows(NullPointerException.class, () -> model.hasTeam(null));
    }

    @Test
    public void hasTeam_teamNotInModel_returnsFalse() {
        ModelManager model = new ModelManager();
        Team team = new TeamBuilder().withId("T0001").withTeamName("Core").build();
        assertFalse(model.hasTeam(team));
    }

    @Test
    public void hasTeam_teamInModel_returnsTrue() {
        ModelManager model = new ModelManager();
        Team team = new TeamBuilder().withId("T0001").withTeamName("Core").build();
        model.addTeam(team);
        assertTrue(model.hasTeam(team));
    }

    @Test
    public void addTeam_addsTeamToAddressBook() {
        ModelManager model = new ModelManager();
        Team team = new TeamBuilder().withId("T0001").withTeamName("Core").build();
        model.addTeam(team);
        assertTrue(model.getAddressBook().getTeamList().contains(team));
    }

    @Test
    public void removeTeam_removesTeamFromAddressBook() {
        ModelManager model = new ModelManager();
        Team team = new TeamBuilder().withId("T0001").withTeamName("Core").build();
        model.addTeam(team);
        model.removeTeam(team);
        assertFalse(model.getAddressBook().getTeamList().contains(team));
    }

    @Test
    public void setTeam_replacesExistingTeam() {
        ModelManager model = new ModelManager();
        Team original = new TeamBuilder().withId("T0001").withTeamName("Core").build();
        model.addTeam(original);

        Team edited = new TeamBuilder(original).withTeamName("Core Renamed").build();
        model.setTeam(original, edited);

        // find team with same id and verify updated name
        boolean foundEditedName = model.getAddressBook().getTeamList().stream()
                .filter(t -> "T0001".equals(t.getId()))
                .anyMatch(t -> "Core Renamed".equals(t.getTeamName().teamName()));
        assertTrue(foundEditedName);
    }

    @Test
    public void getOrganizationHierarchyString_validString() {
        ModelManager model = new ModelManager();
        assertEquals("", model.getOrganizationHierarchyString());
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.name().fullName().split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }

    @Test
    public void addAuditEntry_validEntry_success() {
        ModelManager modelManager = new ModelManager();
        modelManager.addAuditEntry("ADD", "Added person: John Doe");

        AuditLog auditLog = modelManager.getAuditLog();
        assertFalse(auditLog.getEntries().isEmpty());
        assertEquals(1, auditLog.getEntries().size());
        assertEquals("ADD", auditLog.getEntries().get(0).getAction());
        assertEquals("Added person: John Doe", auditLog.getEntries().get(0).getDetails());
    }

    @Test
    public void addAuditEntry_multipleEntries_success() {
        ModelManager modelManager = new ModelManager();
        modelManager.addAuditEntry("ADD", "Added person: John");
        modelManager.addAuditEntry("EDIT", "Edited person: Jane");
        modelManager.addAuditEntry("DELETE", "Deleted person: Bob");

        AuditLog auditLog = modelManager.getAuditLog();
        assertEquals(3, auditLog.getEntries().size());
    }

    @Test
    public void getAuditLog_returnsAddressBookAuditLog() {
        ModelManager modelManager = new ModelManager();
        modelManager.addAuditEntry("TEST", "Test entry");

        AuditLog auditLog = modelManager.getAuditLog();
        assertNotNull(auditLog);
        assertEquals(1, auditLog.getEntries().size());
    }

    @Test
    public void auditLog_persistsAcrossModelOperations() {
        ModelManager modelManager = new ModelManager();
        modelManager.addAuditEntry("ADD", "Initial entry");

        // Perform other model operations
        Person person = new PersonBuilder().withName("Test").build();
        modelManager.addPerson(person);

        // Audit log should still contain the entry
        AuditLog auditLog = modelManager.getAuditLog();
        assertEquals(1, auditLog.getEntries().size());
    }
}
