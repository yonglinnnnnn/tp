package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {
    private static final int INDEX_FIRST_PERSON = 0;
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validEmployeeIdUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON);
        String employeeId = personToDelete.id();
        DeleteCommand deleteCommand = new DeleteCommand(employeeId);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidEmployeeIdUnfilteredList_throwsCommandException() {
        String nonExistentEmployeeId = "E9999";
        DeleteCommand deleteCommand = new DeleteCommand(nonExistentEmployeeId);

        assertCommandFailure(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_PERSON_NOT_FOUND, nonExistentEmployeeId));
    }

    @Test
    public void execute_validEmployeeIdFilteredList_success() {
        model.updateFilteredPersonList(p -> true);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON);
        String employeeId = personToDelete.id();
        DeleteCommand deleteCommand = new DeleteCommand(employeeId);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidEmployeeIdFilteredList_throwsCommandException() {
        String nonExistentEmployeeId = "E8888";
        DeleteCommand deleteCommand = new DeleteCommand(nonExistentEmployeeId);

        assertCommandFailure(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_PERSON_NOT_FOUND, nonExistentEmployeeId));
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand("E0001");
        DeleteCommand deleteSecondCommand = new DeleteCommand("E0002");

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand("E0001");
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different employee ID -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        String employeeId = "E0001";
        DeleteCommand deleteCommand = new DeleteCommand(employeeId);
        String expected = DeleteCommand.class.getCanonicalName() + "{employeeId=" + employeeId + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
