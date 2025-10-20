package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests for {@link CreateTeamCommand}.
 */
public class CreateTeamCommandTest {

    @Test
    public void execute_validLeader_createsTeam() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        Person leader = new PersonBuilder().withId(1).build();
        model.addPerson(leader);

        CreateTeamCommand cmd = new CreateTeamCommand("Team Alpha", "E0001");
        CommandResult result = cmd.execute(model);

        assertEquals(String.format(CreateTeamCommand.MESSAGE_SUCCESS, "Team Alpha"),
                result.getFeedbackToUser());
        assertTrue(model.getAddressBook().getTeamList().stream()
                .anyMatch(t -> "Team Alpha".equals(t.getTeamName().teamName())
                        && "E0001".equals(t.getLeaderId())));
    }

    @Test
    public void execute_leaderNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        CreateTeamCommand cmd = new CreateTeamCommand("Team Alpha", "E9999");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(String.format(CreateTeamCommand.MESSAGE_LEADER_NOT_FOUND, "E9999"), ex.getMessage());
    }

    @Test
    public void execute_duplicateTeam_throwsCommandException() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        Person leader = new PersonBuilder().withId(1).build();
        model.addPerson(leader);

        CreateTeamCommand first = new CreateTeamCommand("Team Alpha", "E0001");
        first.execute(model);

        CreateTeamCommand second = new CreateTeamCommand("Team Alpha", "E0001");
        CommandException ex = assertThrows(CommandException.class, () -> second.execute(model));
        assertEquals(CreateTeamCommand.MESSAGE_DUPLICATE_TEAM, ex.getMessage());
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        CreateTeamCommand a = new CreateTeamCommand("Team Alpha", "E0001");
        CreateTeamCommand b = new CreateTeamCommand("Team Alpha", "E0001");
        assertTrue(a.equals(b));
        assertTrue(a.equals(a));
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        CreateTeamCommand base = new CreateTeamCommand("Team Alpha", "E0001");
        CreateTeamCommand diffName = new CreateTeamCommand("Team Beta", "E0001");
        CreateTeamCommand diffLeader = new CreateTeamCommand("Team Alpha", "E9999");
        assertFalse(base.equals(diffName));
        assertFalse(base.equals(diffLeader));
        assertFalse(base.equals(null));
        assertFalse(base.equals("not a command"));
    }

    @Test
    public void toString_containsFields() {
        CreateTeamCommand cmd = new CreateTeamCommand("Team Alpha", "E0001");
        String s = cmd.toString();
        // Expect the ToStringBuilder to include these keys/values
        assertTrue(s.contains("teamName=Team Alpha"));
        assertTrue(s.contains("leaderPersonId=E0001"));
        // Not a strict full-format assertion; presence of keys+values is sufficient
    }
}
