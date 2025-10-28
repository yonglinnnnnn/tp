package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SetSubteamCommandTest {

    @Test
    void execute_validSubteam_setsSubteamSuccessfully() {
        final boolean[] called = {false};
        final String[] parentCaptured = new String[1];
        final String[] subCaptured = new String[1];

        Model model = new ModelManager(new AddressBook(), new UserPrefs()) {
            @Override
            public boolean setSubteam(String parentId, String subteamId) {
                called[0] = true;
                parentCaptured[0] = parentId;
                subCaptured[0] = subteamId;
                return true;
            }
        };

        SetSubteamCommand command = new SetSubteamCommand("T0001", "T0002");
        CommandResult result;
        try {
            result = command.execute(model);
        } catch (CommandException e) {
            throw new AssertionError("Execution of valid SetSubteamCommand should not fail.", e);
        }

        assertTrue(called[0]);
        assertEquals("T0001", parentCaptured[0]);
        assertEquals("T0002", subCaptured[0]);
        assertEquals(String.format(SetSubteamCommand.MESSAGE_SUCCESS, "T0002", "T0001"),
                result.getFeedbackToUser());
    }

    @Test
    void execute_nullSubteam_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SetSubteamCommand("T0001", null));
    }

    @Test
    void execute_emptySubteamName_throwsIllegalArgumentException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs()) {
            @Override
            public boolean setSubteam(String parentId, String subteamId) {
                throw new IllegalArgumentException("Subteam id cannot be empty");
            }
        };
        assertThrows(IllegalArgumentException.class, () ->
                new SetSubteamCommand("T0001", "").execute(model));
    }

    @Test
    void execute_duplicateSubteamName_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs()) {
            @Override
            public boolean setSubteam(String parentId, String subteamId) {
                return false;
            }
        };
        assertThrows(seedu.address.logic.commands.exceptions.CommandException.class, () ->
                new SetSubteamCommand("T0001", "T0002").execute(model));
    }
}