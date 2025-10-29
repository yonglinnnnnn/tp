package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.team.Team;
import seedu.address.model.team.TeamName;
import seedu.address.testutil.TypicalPersons;
import seedu.address.testutil.TypicalTeams;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteTeamCommand}.
 */
public class DeleteTeamCommandTest {

    private static final String TEAM_ID = "T9999";
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
    }

    @Test
    public void execute_validTeamIdUnfilteredList_success() {
        // create a person that is a member of the team
        Person base = TypicalPersons.BENSON;
        Person personWithTeam = base.duplicate().withTeamIds(Set.of(TEAM_ID)).build();
        model.addPerson(personWithTeam);

        // create the team and add to model
        Team teamToDelete = new Team(TEAM_ID, new TeamName("TempTeam"));
        teamToDelete.withMembers(List.of(personWithTeam.id()));
        model.addTeam(teamToDelete);

        DeleteTeamCommand deleteCommand = new DeleteTeamCommand(TEAM_ID);

        String expectedMessage = String.format(DeleteTeamCommand.MESSAGE_DELETE_TEAM_SUCCESS, TEAM_ID);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        // person should no longer reference the deleted team
        expectedModel.setPerson(personWithTeam, personWithTeam.withRemovedTeam(TEAM_ID));
        expectedModel.removeTeam(teamToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTeamIdUnfilteredList_throwsCommandException() {
        DeleteTeamCommand deleteCommand = new DeleteTeamCommand("T0000");
        assertCommandFailure(deleteCommand, model,
                String.format(DeleteTeamCommand.MESSAGE_TEAM_NOT_FOUND, "T0000"));
    }

    @Test
    public void execute_teamHasSubteams_throwsCommandException() {
        Team child = new Team("T0002", new TeamName("Child"));
        Team parent = new Team("T0001", new TeamName("Parent"));
        parent.withSubteams(List.of(child));

        model.addTeam(parent);
        model.addTeam(child);

        DeleteTeamCommand cmd = new DeleteTeamCommand("T0001");
        assertCommandFailure(cmd, model, String.format(DeleteTeamCommand.MESSAGE_HAS_SUBTEAMS, "T0001"));
    }

    @Test
    public void execute_deleteSubteam_removedFromParentSubteams() throws Exception {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        for (Team t : TypicalTeams.getTypicalTeams()) {
            model.addTeam(t);
        }

        String teamIdToDelete = "T0002"; // BACKEND, a subteam of QA
        String parentId = "T0004"; // QA, parent of BACKEND

        DeleteTeamCommand deleteCommand = new DeleteTeamCommand(teamIdToDelete);

        CommandResult result = deleteCommand.execute(model);

        String expectedMessage = String.format(DeleteTeamCommand.MESSAGE_DELETE_TEAM_SUCCESS, teamIdToDelete);
        assertEquals(expectedMessage, result.getFeedbackToUser());

        Team updatedParent = model.getAddressBook().getTeamList().stream()
                .filter(t -> parentId.equals(t.getId()))
                .findFirst()
                .orElseThrow();

        assertFalse(updatedParent.getSubteams().stream()
                .anyMatch(st -> teamIdToDelete.equals(st.getId())));
    }

    @Test
    public void equals_sameAndDifferentValues() {
        DeleteTeamCommand a = new DeleteTeamCommand("T0001");
        DeleteTeamCommand aCopy = new DeleteTeamCommand("T0001");
        DeleteTeamCommand b = new DeleteTeamCommand("T0002");

        assertTrue(a.equals(aCopy));
        assertFalse(a.equals(b));
        assertFalse(a.equals(null));
        assertTrue(a.equals(a));
    }

    @Test
    public void toStringMethod() {
        String teamId = "T0001";
        DeleteTeamCommand cmd = new DeleteTeamCommand(teamId);
        String expected = DeleteTeamCommand.class.getCanonicalName() + "{teamId=" + teamId + "}";
        assertTrue(cmd.toString().equals(expected));
    }
}
