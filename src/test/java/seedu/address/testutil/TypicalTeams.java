package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.team.Team;
import seedu.address.model.team.TeamName;
import seedu.address.model.team.TeamsManager;

/**
 * Utility class containing typical Team objects for tests.
 */
public class TypicalTeams {

    // Construct teams using TypicalPersons so ids line up with person fixtures.
    public static final Team CORE = new TeamBuilder()
            .withId("T0001")
            .withTeamName(new TeamName("Core"))
            .withTeamLeader(TypicalPersons.BENSON)
            .withMembers(TypicalPersons.BENSON, TypicalPersons.CARL)
            .build();

    public static final Team BACKEND = new TeamBuilder()
            .withId("T0002")
            .withTeamName(new TeamName("Backend"))
            .withParentTeam(CORE)
            .withTeamLeader(TypicalPersons.DANIEL)
            .withMembers(TypicalPersons.DANIEL, TypicalPersons.ELLE)
            .build();

    public static final Team FRONTEND = new TeamBuilder()
            .withId("T0003")
            .withTeamName(new TeamName("Frontend"))
            .withParentTeam(CORE)
            .withTeamLeader(TypicalPersons.ELLE)
            .withMembers(TypicalPersons.ELLE)
            .build();

    public static final Team QA = new TeamBuilder()
            .withId("T0004")
            .withTeamName(new TeamName("QA"))
            .withParentTeam(CORE)
            .withTeamLeader(TypicalPersons.FIONA)
            .withMembers(TypicalPersons.FIONA)
            .withSubteams(FRONTEND, BACKEND)
            .build();

    private TypicalTeams() {}

    public static List<Team> getTypicalTeams() {
        return new ArrayList<>(Arrays.asList(CORE, BACKEND, FRONTEND, QA));
    }

    public static TeamsManager getTypicalTeamsManager() {
        TeamsManager manager = new TeamsManager();
        for (Team t : getTypicalTeams()) {
            manager.addTeam(t);
        }
        return manager;
    }
}