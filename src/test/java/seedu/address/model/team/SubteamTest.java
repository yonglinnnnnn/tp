package seedu.address.model.team;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.team.exceptions.InvalidSubteamNesting;
import seedu.address.model.team.exceptions.TeamNotFoundException;


class SubteamTest {
    private static final String INDEX_FIRST = "T0001";
    private static final String INDEX_SECOND = "T0002";
    private static final String INDEX_THIRD = "T0003";
    private static Team teamA = null;
    private static Team teamB = null;
    private static Team teamC = null;

    @BeforeEach
    void setup() {
        AddressBook ab = new AddressBook();
        Subteams.setAddressBook(ab);
        teamA = new Team("T0001", new TeamName("Team A"));
        teamB = new Team("T0002", new TeamName("Team B"));
        teamC = new Team("T0003", new TeamName("Team C"));
        ab.addTeam(teamA);
        ab.addTeam(teamB);
        ab.addTeam(teamC);
    }

    @Test
    void teamInTopLevelList_returnsTrue() {
        Subteams subteams = new Subteams(Collections.singletonList(INDEX_FIRST));
        assertTrue(subteams.contains(INDEX_FIRST));
    }

    @Test
    void teamNotInList_returnsFalse() {
        teamA.addToSubteam(teamB.getId());
        assertFalse(teamA.containsTeamInSubteams(INDEX_THIRD));
    }

    @Test
    void subteamContainsNonExistentTeamId_throwsTeamNotFoundException() {
        Subteams subteams = new Subteams(Collections.singletonList("T0004"));
        teamA.withSubteams(subteams);
        assertThrows(TeamNotFoundException.class, () -> teamA.containsTeamInSubteams("T0001"));
    }

    @Test
    void teamInNestedSubteams_returnsTrue() {
        Team teamA = new Team(INDEX_FIRST, new TeamName("Example"));
        teamA.addToSubteam(INDEX_SECOND);
        assertTrue(teamA.containsTeamInSubteams(INDEX_SECOND));
    }

    @Test
    void teamInDeeplyNestedSubteams_returnsTrue() {
        teamB.addToSubteam(teamC.getId());
        teamA.addToSubteam(teamB.getId());
        assertTrue(teamA.containsTeamInSubteams(teamC));
    }

    @Test
    void teamNotNested_returnsFalse() {
        Subteams subteams = new Subteams(Collections.singletonList(INDEX_FIRST));
        assertFalse(subteams.contains(INDEX_SECOND));
    }

    @Test
    void cyclicSubteamsGraph_throwsInvalidSubteamNesting() {
        assertThrows(InvalidSubteamNesting.class, () -> {
            teamA.addToSubteam(teamB.getId());
            teamB.addToSubteam(teamA.getId());
        });
    }

    @Test
    void size_returnsCorrectSize() {
        Subteams subteams = new Subteams();
        subteams.add(INDEX_FIRST, "A");
        subteams.add(INDEX_SECOND, "B");
        assertEquals(2, subteams.size());
    }

    @Test
    void nullTeam_throwsNullPointerException() {
        Subteams subteams = new Subteams();
        assertThrows(NullPointerException.class, () -> subteams.contains(null));
    }

    @Test
    void removeTeam_successfulRemoval() {
        Subteams subteams = new Subteams();
        subteams.add(INDEX_FIRST, "A");
        boolean removed = subteams.remove(INDEX_FIRST);
        assertTrue(removed);
    }

    @Test
    void sameSubteams_returnsTrue() {
        Subteams subteams1 = new Subteams(Collections.singletonList(INDEX_FIRST));
        Subteams subteams2 = new Subteams(Collections.singletonList(INDEX_FIRST));
        assertEquals(subteams1, subteams2);
    }
}
