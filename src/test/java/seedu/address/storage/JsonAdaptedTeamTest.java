package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.team.Team;
import seedu.address.model.team.TeamName;

public class JsonAdaptedTeamTest {
    private static final String VALID_ID = "T0001";
    private static final String VALID_NAME = "Core";
    private static final String VALID_LEADER_ID = "E1001";
    private static final List<String> VALID_MEMBERS = new ArrayList<>() {
        {
            add("E1001");
            add("E1002");
        }
    };

    private static final Team TEAM_WITH_LEADER_AND_MEMBERS = new Team(VALID_ID, new TeamName(VALID_NAME))
            .withLeader(VALID_LEADER_ID)
            .withMembers(VALID_MEMBERS);

    @Test
    public void toModelType_validTeamDetails_returnsTeam() throws Exception {
        JsonAdaptedTeam team = new JsonAdaptedTeam(TEAM_WITH_LEADER_AND_MEMBERS);
        Team modelTeam = team.toModelType();
        assertEquals(TEAM_WITH_LEADER_AND_MEMBERS.getId(), modelTeam.getId());
        assertEquals(TEAM_WITH_LEADER_AND_MEMBERS.getTeamName(), modelTeam.getTeamName());
        assertEquals(TEAM_WITH_LEADER_AND_MEMBERS.getLeaderId(), modelTeam.getLeaderId());
        assertEquals(TEAM_WITH_LEADER_AND_MEMBERS.getMembers(), modelTeam.getMembers());
    }


    @Test
    public void toModelType_nullId_throwsIllegalValueException() {
        JsonAdaptedTeam team = new JsonAdaptedTeam(null, VALID_NAME, VALID_LEADER_ID, VALID_MEMBERS);
        String expectedMessage = String.format(JsonAdaptedTeam.MISSING_FIELD_MESSAGE_FORMAT, "id");
        assertThrows(IllegalValueException.class, expectedMessage, team::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedTeam team = new JsonAdaptedTeam(VALID_ID, null, VALID_LEADER_ID, VALID_MEMBERS);
        String expectedMessage = String.format(JsonAdaptedTeam.MISSING_FIELD_MESSAGE_FORMAT,
                TeamName.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, team::toModelType);
    }

    @Test
    public void toModelType_nullMembers_success() throws Exception {
        JsonAdaptedTeam team = new JsonAdaptedTeam(VALID_ID, VALID_NAME, VALID_LEADER_ID, null);
        Team modelTeam = team.toModelType();
        assertEquals(VALID_ID, modelTeam.getId());
        assertEquals(new TeamName(VALID_NAME), modelTeam.getTeamName());
        assertEquals(VALID_LEADER_ID, modelTeam.getLeaderId());
        assertEquals(new ArrayList<>(), modelTeam.getMembers());
    }
}
