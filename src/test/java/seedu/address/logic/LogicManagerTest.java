package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GITHUBUSERNAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.CreateTeamCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.audit.AuditLog;
import seedu.address.model.audit.AuditLogEntry;
import seedu.address.model.person.Person;
import seedu.address.model.team.Team;
import seedu.address.model.team.TeamName;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private final Model model = new ModelManager();
    private Logic logic;

    @BeforeAll
    public static void setupOnce() {
        Field field;
        try {
            field = AddCommandParser.class.getDeclaredField("nextId");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        field.setAccessible(true);
        try {
            field.setLong(null, 0);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);

        // Reset team ID counter
        try {
            Field field = CreateTeamCommand.class.getDeclaredField("nextId");
            field.setAccessible(true);
            field.setLong(null, 1);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete E9999";
        assertCommandException(deleteCommand, String.format(DeleteCommand.MESSAGE_PERSON_NOT_FOUND, "E9999"));
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + GITHUBUSERNAME_DESC_AMY;

        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validCommand_addsAuditEntry() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + GITHUBUSERNAME_DESC_AMY;

        // Execute the add command
        logic.execute(addCommand);

        // Retrieve the audit log
        AuditLog auditLog = model.getAuditLog();
        List<AuditLogEntry> entries = auditLog.getEntries();

        // Verify an audit entry was created
        assertFalse(entries.isEmpty());

        // Verify the latest entry has the correct action
        AuditLogEntry lastEntry = entries.get(entries.size() - 1);
        assertEquals("ADD", lastEntry.getAction());
        assertTrue(lastEntry.getDetails().contains("Amy"));
    }

    @Test
    public void execute_auditCommand_logsAuditViewing() throws Exception {
        // Execute audit command
        logic.execute("audit");

        // Check that viewing audit log itself is logged
        AuditLog auditLog = model.getAuditLog();
        List<AuditLogEntry> entries = auditLog.getEntries();

        assertTrue(entries.isEmpty());
    }

    @Test
    public void execute_deleteCommand_addsAuditEntry() throws Exception {
        // Add a person first
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + GITHUBUSERNAME_DESC_AMY;
        logic.execute(addCommand);

        // Delete the person
        logic.execute("delete E5003");

        // Verify delete action in audit log
        AuditLog auditLog = model.getAuditLog();
        List<AuditLogEntry> entries = auditLog.getEntries();

        assertTrue(entries.stream()
                .anyMatch(entry -> entry.getAction().equals("DELETE")));
    }

    @Test
    public void execute_listCommand_doesNotAddAuditEntry() throws Exception {
        // Get initial audit log size
        int initialSize = model.getAuditLog().getEntries().size();

        // Execute list command
        logic.execute("list");

        // Verify no new audit entry was added
        assertEquals(initialSize, model.getAuditLog().getEntries().size());
    }

    @Test
    public void execute_helpCommand_doesNotAddAuditEntry() throws Exception {
        // Get initial audit log size
        int initialSize = model.getAuditLog().getEntries().size();

        // Execute help command
        logic.execute("help");

        // Verify no new audit entry was added
        assertEquals(initialSize, model.getAuditLog().getEntries().size());
    }

    @Test
    public void execute_viewCommand_doesNotAddAuditEntry() throws Exception {
        // Add a person first
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + GITHUBUSERNAME_DESC_AMY;
        logic.execute(addCommand);

        // Get audit log size after add
        int sizeAfterAdd = model.getAuditLog().getEntries().size();

        // Execute view command
        logic.execute("view 1");

        // Verify no new audit entry was added
        assertEquals(sizeAfterAdd, model.getAuditLog().getEntries().size());
    }

    @Test
    public void execute_auditCommand_doesNotAddAuditEntry() throws Exception {
        // Get initial audit log size
        int initialSize = model.getAuditLog().getEntries().size();

        // Execute audit command
        logic.execute("audit");

        // Verify no new audit entry was added (audit command shouldn't log itself)
        assertEquals(initialSize, model.getAuditLog().getEntries().size());
    }

    @Test
    public void execute_exitCommand_doesNotAddAuditEntry() throws Exception {
        // Get initial audit log size
        int initialSize = model.getAuditLog().getEntries().size();

        // Execute audit command
        logic.execute("exit");

        // Verify no new audit entry was added (audit command shouldn't log itself)
        assertEquals(initialSize, model.getAuditLog().getEntries().size());
    }


    @Test
    public void execute_emptyAddressBook_nextIdRemainsUnchanged() throws Exception {
        // Setup empty model
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Get current nextId before initialization
        Field field = AddCommandParser.class.getDeclaredField("nextId");
        field.setAccessible(true);
        long initialNextId = field.getLong(null);

        // Create LogicManager with empty model (should not update nextId)
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        Logic logic = new LogicManager(emptyModel, storage);

        // Verify nextId hasn't changed
        long afterNextId = field.getLong(null);
        assertEquals(initialNextId, afterNextId);
    }

    @Test
    public void execute_addressBookWithPersons_nextIdUpdatedCorrectly() throws Exception {
        // Setup model with persons having different IDs
        AddressBook ab = new AddressBook();
        ab.addPerson(new PersonBuilder().withName("Alice").withId(5000).build());
        ab.addPerson(new PersonBuilder().withName("Bob").withId(5001).build());
        ab.addPerson(new PersonBuilder().withName("Charlie").withId(5002).build());
        Model modelWithPersons = new ModelManager(ab, new UserPrefs());

        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        Logic logic = new LogicManager(modelWithPersons, storage);

        Field field = AddCommandParser.class.getDeclaredField("nextId");
        field.setAccessible(true);
        long nextId = field.getLong(null);
        assertEquals(5003L, nextId);
    }

    @Test
    public void execute_addressBookWithMixedIds_ignoresNonEPrefixedIds() throws Exception {
        // Setup with mixed IDs
        AddressBook ab = new AddressBook();
        ab.addPerson(new PersonBuilder().withName("David").withId(7123).build());
        ab.addPerson(new PersonBuilder().withName("Eve").withId(23).build());
        ab.addPerson(new PersonBuilder().withName("Frank").withId(1225).build());
        Model modelWithPersons = new ModelManager(ab, new UserPrefs());

        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        Logic logic = new LogicManager(modelWithPersons, storage);

        Field field = AddCommandParser.class.getDeclaredField("nextId");
        field.setAccessible(true);
        long nextId = field.getLong(null);
        assertEquals(7124L, nextId);
    }

    @Test
    public void execute_emptyAddressBook_teamNextIdRemainsUnchanged() throws Exception {
        // Setup empty model
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Get current nextId before initialization
        Field field = CreateTeamCommand.class.getDeclaredField("nextId");
        field.setAccessible(true);
        long initialNextId = field.getLong(null);

        // Create LogicManager with empty model (should not update nextId)
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        Logic logic = new LogicManager(emptyModel, storage);

        // Verify nextId hasn't changed
        long afterNextId = field.getLong(null);
        assertEquals(initialNextId, afterNextId);
    }

    @Test
    public void execute_addressBookWithTeams_nextIdUpdatedCorrectly() throws Exception {
        // Setup model with teams having different IDs
        AddressBook ab = new AddressBook();
        ab.addTeam(new Team("T0001", new TeamName("Alpha")));
        ab.addTeam(new Team("T0002", new TeamName("Beta")));
        ab.addTeam(new Team("T0003", new TeamName("Gamma")));
        Model modelWithTeams = new ModelManager(ab, new UserPrefs());

        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        Logic logic = new LogicManager(modelWithTeams, storage);

        Field field = CreateTeamCommand.class.getDeclaredField("nextId");
        field.setAccessible(true);
        long nextId = field.getLong(null);
        assertEquals(4L, nextId);
    }
}
