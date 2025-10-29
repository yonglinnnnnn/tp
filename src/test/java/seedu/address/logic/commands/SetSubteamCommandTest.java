package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.team.Team;
import seedu.address.model.team.TeamName;

class SetSubteamCommandTest {
    private static final String INDEX_FIRST = "T0001";
    private static final String INDEX_SECOND = "T0002";

    @Test
    void execute_validSubteam_setsSubteamSuccessfully() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addTeam(new Team(INDEX_FIRST, new TeamName("A")));
        model.addTeam(new Team(INDEX_SECOND, new TeamName("B")));

        SetSubteamCommand command = new SetSubteamCommand(INDEX_FIRST, INDEX_SECOND);
        CommandResult result;
        try {
            result = command.execute(model);
        } catch (CommandException e) {
            throw new AssertionError("Execution of valid SetSubteamCommand should not fail.", e);
        }

        assertEquals(String.format(SetSubteamCommand.MESSAGE_SUCCESS, INDEX_SECOND, INDEX_FIRST),
                result.getFeedbackToUser());
    }

    @Test
    void execute_nullSubteam_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SetSubteamCommand(INDEX_FIRST, null));
    }

    @Test
    void execute_emptySubteamName_throwsIllegalArgumentException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs()) {
            @Override
            public boolean setSubteam(String parentId, String subteamId) {
                throw new IllegalArgumentException("Subteam id cannot be empty");
            }
        };
        assertThrows(CommandException.class, () ->
                new SetSubteamCommand(INDEX_FIRST, "").execute(model));
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
                new SetSubteamCommand(INDEX_FIRST, INDEX_SECOND).execute(model));
    }

    @Test
    void execute_setSubteamFail_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs()) {
            @Override
            public boolean setSubteam(String parentId, String subteamId) {
                return false;
            }
        };
        assertThrows(CommandException.class, () ->
                new SetSubteamCommand(INDEX_FIRST, INDEX_SECOND).execute(model));
    }

    @Test
    void equals() {
        SetSubteamCommand commandA = new SetSubteamCommand("T0001", "T0002");
        SetSubteamCommand commandB = new SetSubteamCommand("T0001", "T0002");
        SetSubteamCommand commandC = new SetSubteamCommand("T0002", "T0003");

        // same object -> returns true
        assertEquals(commandA, commandA);

        // same values -> returns true
        assertEquals(commandA, commandB);

        // different types -> returns false
        assertNotEquals("T123", commandA);

        // different team -> returns false
        assertNotEquals(commandA, commandC);
    }

    @Test
    void toString_validSubteamCommand_returnsCorrectString() {
        SetSubteamCommand command = new SetSubteamCommand("T0001", "T0002");
        String expectedString = "seedu.address.logic.commands.SetSubteamCommand{parentTeamId=T0001, subteamId=T0002}";
        String actualString = command.toString();
        assertEquals(expectedString, actualString);
    }
}
