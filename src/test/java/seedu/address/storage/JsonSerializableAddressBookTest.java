package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalTeams.CORE;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.audit.AuditLog;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.team.Team;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalPersons.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalPersonsAddressBook);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsDuplicateException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(DuplicatePersonException.class, "Operation would result in duplicate persons",
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_typicalTeamsFile_success() throws Exception {
        AddressBook original = TypicalPersons.getTypicalAddressBook();
        original.addTeam(CORE);

        JsonSerializableAddressBook jsonBook = new JsonSerializableAddressBook(original);
        AddressBook converted = jsonBook.toModelType();
        List<Team> teams = converted.getTeamList();

        assertEquals(1, teams.size());
        assertEquals("Core", teams.get(0).getTeamName().teamName());
    }

    @Test
    public void toModelType_validAuditLog_success() throws Exception {
        AddressBook original = TypicalPersons.getTypicalAddressBook();
        original.addAuditEntry("ADD", "Added person: Alice");
        original.addAuditEntry("EDIT", "Edited person: Bob");

        JsonSerializableAddressBook jsonBook = new JsonSerializableAddressBook(original);
        AddressBook converted = jsonBook.toModelType();
        AuditLog auditLog = converted.getAuditLog();

        assertEquals(2, auditLog.getEntries().size());
        assertEquals("ADD", auditLog.getEntries().get(0).getAction());
        assertEquals("Added person: Alice", auditLog.getEntries().get(0).getDetails());
    }

    @Test
    public void toModelType_emptyAuditLog_success() throws Exception {
        AddressBook original = TypicalPersons.getTypicalAddressBook();
        JsonSerializableAddressBook jsonBook = new JsonSerializableAddressBook(original);
        AddressBook converted = jsonBook.toModelType();
        AuditLog auditLog = converted.getAuditLog();

        assertNotNull(auditLog);
        assertTrue(auditLog.getEntries().isEmpty());
    }

    @Test
    public void toModelType_auditLogPersistence_success() throws Exception {
        AddressBook original = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        original.addAuditEntry("TEST", "Test action");
        original.addAuditEntry("DELETE", "Deleted person");

        JsonSerializableAddressBook jsonBook = new JsonSerializableAddressBook(original);
        AddressBook converted = jsonBook.toModelType();

        assertEquals(2, converted.getAuditLog().getEntries().size());
        assertEquals("TEST", converted.getAuditLog().getEntries().get(0).getAction());
        assertEquals("Test action", converted.getAuditLog().getEntries().get(0).getDetails());
    }

    @Test
    public void toModelType_multipleAuditEntries_correctOrder() throws Exception {
        AddressBook original = new AddressBook();
        original.addAuditEntry("FIRST", "First entry");
        original.addAuditEntry("SECOND", "Second entry");
        original.addAuditEntry("THIRD", "Third entry");

        JsonSerializableAddressBook jsonBook = new JsonSerializableAddressBook(original);
        AddressBook converted = jsonBook.toModelType();

        assertEquals(3, converted.getAuditLog().getEntries().size());
        assertEquals("FIRST", converted.getAuditLog().getEntries().get(0).getAction());
        assertEquals("SECOND", converted.getAuditLog().getEntries().get(1).getAction());
        assertEquals("THIRD", converted.getAuditLog().getEntries().get(2).getAction());
    }
}
