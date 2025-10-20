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
import seedu.address.testutil.TeamBuilder;

/**
 * Tests for {@link AddToTeamCommand}.
 */
public class AddToTeamCommandTest {

    @Test
    public void execute_addsMemberAndUpdatesPerson() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        Person p = new PersonBuilder().withId(1).build();
        model.addPerson(p);

        model.addTeam(new TeamBuilder().withId("T0001").withTeamName("Core").build());

        AddToTeamCommand cmd = new AddToTeamCommand("T0001", "E0001");
        CommandResult result = cmd.execute(model);

        assertEquals(String.format(AddToTeamCommand.MESSAGE_SUCCESS, "E0001", "T0001"),
                result.getFeedbackToUser());

        assertTrue(model.getAddressBook().getTeamList().stream()
                .filter(t -> "T0001".equals(t.getId()))
                .findFirst()
                .map(t -> t.getMembers().contains("E0001"))
                .orElse(false));

        Person updated = model.find(person -> "E0001".equals(person.id()));
        assertTrue(updated.teamIds().contains("T0001"));
    }

    @Test
    public void execute_personNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addTeam(new TeamBuilder().withId("T0001").build());

        AddToTeamCommand cmd = new AddToTeamCommand("T0001", "E9999");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(String.format(AddToTeamCommand.MESSAGE_PERSON_NOT_FOUND, "E9999"), ex.getMessage());
    }

    @Test
    public void execute_teamNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        Person p = new PersonBuilder().withId(1).build();
        model.addPerson(p);

        AddToTeamCommand cmd = new AddToTeamCommand("T9999", "E0001");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(String.format(AddToTeamCommand.MESSAGE_TEAM_NOT_FOUND, "T9999"), ex.getMessage());
    }

    @Test
    public void execute_alreadyMember_throwsCommandException() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        Person p = new PersonBuilder().withId(1).build();
        model.addPerson(p);
        model.addTeam(new TeamBuilder().withId("T0001").withMembers("E0001").build());

        AddToTeamCommand cmd = new AddToTeamCommand("T0001", "E0001");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(String.format(AddToTeamCommand.MESSAGE_ALREADY_MEMBER, "E0001", "T0001"), ex.getMessage());
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        AddToTeamCommand a = new AddToTeamCommand("T0001", "E0001");
        AddToTeamCommand b = new AddToTeamCommand("T0001", "E0001");
        assertTrue(a.equals(b));
        assertTrue(a.equals(a));
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        AddToTeamCommand base = new AddToTeamCommand("T0001", "E0001");
        AddToTeamCommand diffTeam = new AddToTeamCommand("T0002", "E0001");
        AddToTeamCommand diffPerson = new AddToTeamCommand("T0001", "E9999");
        assertFalse(base.equals(diffTeam));
        assertFalse(base.equals(diffPerson));
        assertFalse(base.equals(null));
        assertFalse(base.equals(123));
    }

    @Test
    public void toString_containsIds() {
        AddToTeamCommand cmd = new AddToTeamCommand("T0001", "E0001");
        String s = cmd.toString();
        // Expect toString to contain teamId and personId labels/values
        assertTrue(s.contains("teamId"));
        assertTrue(s.contains("T0001"));
        assertTrue(s.contains("personId"));
        assertTrue(s.contains("E0001"));
    }
}
