package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.SetSalaryCommand.MESSAGE_SUCCESS;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

class SetSalaryCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    void execute_validIdAndSalary_success() {
        SetSalaryCommand command = new SetSalaryCommand("E0001", 100);
        assertCommandSuccess(command, model, String.format(MESSAGE_SUCCESS, 100, "E0001"), model);
    }

    @Test
    void equals() {
        SetSalaryCommand command = new SetSalaryCommand("E12345", 100);
        SetSalaryCommand sameCommand = new SetSalaryCommand("E12345", 100);
        assertEquals(command, sameCommand);
    }
}
