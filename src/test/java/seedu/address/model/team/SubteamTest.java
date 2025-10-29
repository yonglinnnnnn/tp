package seedu.address.model.team;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.team.exceptions.InvalidSubteamNesting;

class SubteamTest {
    private static final String INDEX_FIRST = "T0001";
    private static final String INDEX_SECOND = "T0002";
    private static final String INDEX_THIRD = "T0003";

    @Test
    void teamInTopLevelList_returnsTrue() {
        Team teamA = new Team(INDEX_FIRST, new TeamName("Example"));
        Subteams subteams = new Subteams(Collections.singletonList(teamA));
        assertTrue(subteams.contains(teamA));
    }

    @Test
    void teamNotInList_returnsFalse() {
        Team teamA = new Team(INDEX_FIRST, new TeamName("Example"));
        Team teamB = new Team(INDEX_SECOND, new TeamName("Example"));
        Subteams subteams = new Subteams(Collections.singletonList(teamA));
        assertFalse(subteams.contains(teamB));
    }

    @Test
    void teamInNestedSubteams_returnsTrue() {
        Team teamA = new Team(INDEX_FIRST, new TeamName("Example"));
        Team teamB = new Team(INDEX_SECOND, new TeamName("Example"));
        Team teamANew = teamA.addToSubteam(teamB);
        Subteams subteams = new Subteams(Collections.singletonList(teamANew));
        assertTrue(subteams.contains(teamB));
    }

    @Test
    void teamInDeeplyNestedSubteams_returnsTrue() {
        Team teamA = new Team(INDEX_FIRST, new TeamName("Example"));
        Team teamB = new Team(INDEX_SECOND, new TeamName("Example"));
        Team teamC = new Team(INDEX_THIRD, new TeamName("Example"));
        Team teamBNew = teamB.addToSubteam(teamC);
        Team teamANew = teamA.addToSubteam(teamBNew);
        Subteams subteams = new Subteams(Collections.singletonList(teamANew));
        assertTrue(subteams.contains(teamC));
    }

    @Test
    void teamNotNested_returnsFalse() {
        Team teamA = new Team(INDEX_FIRST, new TeamName("Example"));
        Team teamB = new Team(INDEX_SECOND, new TeamName("Example"));
        Subteams subteams = new Subteams(Collections.singletonList(teamA));
        assertFalse(subteams.contains(teamB));
    }

    @Test
    void cyclicSubteamsGraph_throwsInvalidSubteamNesting() {
        Team teamA = new Team(INDEX_FIRST, new TeamName("Example"));
        Team teamB = new Team(INDEX_SECOND, new TeamName("Example"));
        assertThrows(InvalidSubteamNesting.class, () -> {
            teamA.addToSubteam(teamB);
            teamB.addToSubteam(teamA);
        });
    }

    @Test
    void size_returnsCorrectSize() {
        Team teamA = new Team(INDEX_FIRST, new TeamName("Example"));
        Team teamB = new Team(INDEX_SECOND, new TeamName("Example"));
        Subteams subteams = new Subteams();
        subteams.add(teamA);
        subteams.add(teamB);
        assertEquals(2, subteams.size());
    }

    @Test
    void nullTeam_throwsNullPointerException() {
        Subteams subteams = new Subteams();
        assertThrows(NullPointerException.class, () -> subteams.contains(null));
    }

    @Test
    void removeTeam_successfulRemoval() {
        Team teamA = new Team(INDEX_FIRST, new TeamName("Example"));
        Subteams subteams = new Subteams();
        subteams.add(teamA);
        boolean removed = subteams.remove(teamA);
        assertTrue(removed);
        assertFalse(subteams.contains(teamA));
    }

    @Test
    void sameSubteams_returnsTrue() {
        Team teamA = new Team(INDEX_FIRST, new TeamName("Example"));
        Subteams subteams1 = new Subteams(Collections.singletonList(teamA));
        Subteams subteams2 = new Subteams(Collections.singletonList(teamA));
        assertEquals(subteams1, subteams2);
    }
}
