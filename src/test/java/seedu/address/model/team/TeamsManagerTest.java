// java
package seedu.address.model.team;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.testutil.TypicalTeams;

/**
 * Unit tests for {@link seedu.address.model.team.TeamsManager}.
 *
 * <p>Exercises all public operations: add, remove, replace, lookup and view semantics.
 */
public class TeamsManagerTest {

    private TeamsManager manager;

    @BeforeEach
    public void setUp() {
        manager = new TeamsManager();
    }

    @Test
    public void addTeam_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> manager.addTeam(null));
    }

    @Test
    public void addTeam_addsAndIgnoresDuplicate() {
        Team t = new Team("T1000", "Example");
        manager.addTeam(t);
        List<Team> listAfterAdd = manager.getTeamList();
        assertTrue(listAfterAdd.contains(t));
        int sizeAfterFirstAdd = listAfterAdd.size();

        // adding duplicate (same reference) should be ignored
        manager.addTeam(t);
        assertEquals(sizeAfterFirstAdd, manager.getTeamList().size());
    }

    @Test
    public void removeTeam_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> manager.removeTeam(null));
    }

    @Test
    public void removeTeam_removesIfPresent() {
        Team t = new Team("T1001", "RemoveMe");
        manager.addTeam(t);
        assertTrue(manager.getTeamList().contains(t));
        manager.removeTeam(t);
        assertFalse(manager.getTeamList().contains(t));
    }

    @Test
    public void setTeam_replacesTarget_whenTargetExists() {
        Team original = new Team("T2000", "Orig");
        manager.addTeam(original);

        Team edited = new Team("T2000", "Edited");
        manager.setTeam(original, edited);

        assertFalse(manager.getTeamList().contains(original));
        assertTrue(manager.getTeamList().contains(edited));
    }

    @Test
    public void setTeam_nullTargetOrEdited_throwsNullPointerException() {
        Team t = new Team("T3000", "Some");
        manager.addTeam(t);
        assertThrows(NullPointerException.class, () -> manager.setTeam(null, t));
        assertThrows(NullPointerException.class, () -> manager.setTeam(t, null));
    }

    @Test
    public void setTeam_targetNotFound_throwsIllegalArgumentException() {
        Team missing = new Team("T9999", "Missing");
        Team edited = new Team("T9999", "Edited");
        assertThrows(IllegalArgumentException.class, () -> manager.setTeam(missing, edited));
    }

    @Test
    public void hasTeam_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> manager.hasTeam(null));
    }

    @Test
    public void hasTeam_returnsTrueWhenPresent_falseWhenAbsent() {
        Team t = new Team("T4000", "Exists");
        assertFalse(manager.hasTeam(t));
        manager.addTeam(t);
        assertTrue(manager.hasTeam(t));
    }

    @Test
    public void getTeamById_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> manager.getTeamById(null));
    }

    @Test
    public void getTeamById_returnsOptionalCorrectly() {
        Team t = new Team("T5000", "Lookup");
        manager.addTeam(t);
        Optional<Team> opt = manager.getTeamById("T5000");
        assertTrue(opt.isPresent());
        assertEquals(t, opt.get());

        Optional<Team> missing = manager.getTeamById("NO_SUCH_ID");
        assertFalse(missing.isPresent());
    }

    @Test
    public void getTeamList_isUnmodifiable() {
        Team t = new Team("T6000", "Immutable");
        manager.addTeam(t);
        List<Team> list = manager.getTeamList();
        assertThrows(UnsupportedOperationException.class, () -> list.add(new Team("TX", "X")));
    }

    @Test
    public void resetTeams_clearsAll() {
        manager = TypicalTeams.getTypicalTeamsManager();
        assertFalse(manager.getTeamList().isEmpty());
        manager.resetTeams();
        assertTrue(manager.getTeamList().isEmpty());
    }


    @Test
    public void getTeamList_returnsUnmodifiableView_consistentWithContents() {
        Team t = new Team("T8000", "ViewTest");
        manager.addTeam(t);
        List<Team> view = manager.getTeamList();
        assertEquals(1, view.size());
        assertEquals(t, view.get(0));
    }
}
