package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.team.Team;
import seedu.address.model.team.TeamName;

/**
 * Test fixtures for {@link seedu.address.model.team.Team} objects.
 *
 * for use in unit tests.
 */
public class TypicalTeams {

    // Construct teams using TypicalPersons so ids line up with person fixtures.
    public static final Team CORE = new TeamBuilder()
            .withId("T0001")
            .withTeamName(new TeamName("Core"))
            .withLeader(TypicalPersons.BENSON.id())
            .withMembers(TypicalPersons.BENSON.id(), TypicalPersons.CARL.id())
            .build();

    public static final Team BACKEND = new TeamBuilder()
            .withId("T0002")
            .withTeamName(new TeamName("Backend"))
            .withParentTeamId(CORE.getParentTeamId())
            .withLeader(TypicalPersons.DANIEL.id())
            .withMembers(TypicalPersons.DANIEL.id(), TypicalPersons.ELLE.id())
            .build();

    public static final Team FRONTEND = new TeamBuilder()
            .withId("T0003")
            .withTeamName(new TeamName("Frontend"))
            .withParentTeamId(CORE.getParentTeamId())
            .withLeader(TypicalPersons.ELLE.id())
            .withMembers(TypicalPersons.ELLE.id())
            .build();

    public static final Team QA = new TeamBuilder()
            .withId("T0004")
            .withTeamName(new TeamName("QA"))
            .withParentTeamId(CORE.getParentTeamId())
            .withLeader(TypicalPersons.FIONA.id())
            .withMembers(TypicalPersons.FIONA.id())
            .build();

    private TypicalTeams() {}

    /**
     * Returns a list of typical teams.
     */
    public static List<Team> getTypicalTeams() {
        return new ArrayList<>(Arrays.asList(CORE, BACKEND, FRONTEND, QA));
    }

}
