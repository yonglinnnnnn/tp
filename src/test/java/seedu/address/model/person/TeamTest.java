package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TeamTest {
    private static final String VALID_TEAM_NAME = "team1";
    private static final String INVALID_TEAM_NAME = "team1!?";

    @Test
    void isValidTeamName() {
        assertTrue(Team.isValidTeamName(VALID_TEAM_NAME));
        assertFalse(Team.isValidTeamName(INVALID_TEAM_NAME));
    }

    @Test
    void name() {
        assertEquals(VALID_TEAM_NAME, new Team(VALID_TEAM_NAME, "Tteam1").name());
    }

    @Test
    void id() {
        assertEquals("Tteam1", new Team(VALID_TEAM_NAME, "Tteam1").id());
    }
}
