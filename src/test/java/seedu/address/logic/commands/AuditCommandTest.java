package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AuditCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_emptyAuditLog_success() {
        AuditCommand auditCommand = new AuditCommand();
        CommandResult result = auditCommand.execute(model);

        assertTrue(result.getFeedbackToUser().contains("No audit log entries found")
                || result.getFeedbackToUser().contains("Audit Log:"));
    }

    @Test
    public void execute_withAuditEntries_success() {
        // Add some audit entries
        model.addAuditEntry("ADD", "Added person: John Doe (ID: E1001)");
        model.addAuditEntry("EDIT", "Edited person: Jane Smith (ID: E1002)");
        model.addAuditEntry("DELETE", "Deleted person: Bob Lee (ID: E1003)");

        AuditCommand auditCommand = new AuditCommand();
        CommandResult result = auditCommand.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Audit Log:"));
        assertTrue(feedback.contains("ADD"));
        assertTrue(feedback.contains("EDIT"));
        assertTrue(feedback.contains("DELETE"));
        assertTrue(feedback.contains("John Doe"));
        assertTrue(feedback.contains("Jane Smith"));
        assertTrue(feedback.contains("Bob Lee"));
    }

    @Test
    public void execute_multipleEntriesSameAction_success() {
        model.addAuditEntry("ADD", "Added person: Person 1");
        model.addAuditEntry("ADD", "Added person: Person 2");
        model.addAuditEntry("ADD", "Added person: Person 3");

        AuditCommand auditCommand = new AuditCommand();
        CommandResult result = auditCommand.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Person 1"));
        assertTrue(feedback.contains("Person 2"));
        assertTrue(feedback.contains("Person 3"));
    }

    @Test
    public void execute_auditLogPreservedAcrossOperations_success() {
        // Perform multiple operations
        Person person = new PersonBuilder().withName("Test Person").build();
        model.addPerson(person);
        model.addAuditEntry("ADD", "Added person: Test Person");

        model.deletePerson(person);
        model.addAuditEntry("DELETE", "Deleted person: Test Person");

        AuditCommand auditCommand = new AuditCommand();
        CommandResult result = auditCommand.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("ADD"));
        assertTrue(feedback.contains("DELETE"));
    }

    @Test
    public void equals() {
        AuditCommand auditCommand1 = new AuditCommand();
        AuditCommand auditCommand2 = new AuditCommand();

        // same object -> returns true
        assertTrue(auditCommand1.equals(auditCommand1));

        // same type -> returns true
        assertTrue(auditCommand1.equals(auditCommand2));

        // null -> returns false
        assertFalse(auditCommand1.equals(null));

        // different types -> returns false
        assertFalse(auditCommand1.equals(new ClearCommand()));
    }

    @Test
    public void toString_returnsCorrectString() {
        AuditCommand auditCommand = new AuditCommand();
        String expected = new ToStringBuilder(auditCommand).toString();
        assertEquals(expected, auditCommand.toString());
    }
}
