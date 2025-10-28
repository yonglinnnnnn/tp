package seedu.address.model.team;

import seedu.address.model.team.exceptions.InvalidSubteamNesting;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

class SubteamTest {

    @Test
    void contains_teamInTopLevelList_returnsTrue() {
        Team teamA = new Team("T1000", new TeamName("Example"));
        Subteams subteams = new Subteams(Collections.singletonList(teamA));
        assertTrue(subteams.contains(teamA));
    }

    @Test
    void contains_teamNotInList_returnsFalse() {
        Team teamA = new Team("T1000", new TeamName("Example"));
        Team teamB = new Team("T2000", new TeamName("Example"));
        Subteams subteams = new Subteams(Collections.singletonList(teamA));
        assertFalse(subteams.contains(teamB));
    }

    @Test
    void contains_teamInNestedSubteams_returnsTrue() {
        Team teamA = new Team("T1000", new TeamName("Example"));
        Team teamB = new Team("T2000", new TeamName("Example"));
        Team teamANew = teamA.addToSubteam(teamB);
        Subteams subteams = new Subteams(Collections.singletonList(teamANew));
        assertTrue(subteams.contains(teamB));
    }

    @Test
    void contains_teamInDeeplyNestedSubteams_returnsTrue() {
        Team teamA = new Team("T1000", new TeamName("Example"));
        Team teamB = new Team("T2000", new TeamName("Example"));
        Team teamC = new Team("T3000", new TeamName("Example"));
        Team teamBNew = teamB.addToSubteam(teamC);
        Team teamANew = teamA.addToSubteam(teamBNew);
        Subteams subteams = new Subteams(Collections.singletonList(teamANew));
        assertTrue(subteams.contains(teamC));
    }

    @Test
    void contains_cyclicSubteamsGraph_throwsInvalidSubteamNesting() {
        Team teamA = new Team("T1000", new TeamName("Example"));
        Team teamB = new Team("T2000", new TeamName("Example"));
        assertThrows(InvalidSubteamNesting.class, () -> {
            teamA.addToSubteam(teamB);
            teamB.addToSubteam(teamA);
        });
    }

    @Test
    void contains_nullTeam_throwsNullPointerException() {
        Subteams subteams = new Subteams();
        assertThrows(NullPointerException.class, () -> subteams.contains(null));
    }
}
