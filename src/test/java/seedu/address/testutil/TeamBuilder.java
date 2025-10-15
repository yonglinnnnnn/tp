package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import seedu.address.model.person.Person;
import seedu.address.model.team.Team;
import seedu.address.model.team.TeamName;

/**
 * A utility class to help build Team objects for tests.
 */
public class TeamBuilder {
    public static final String DEFAULT_ID = "T0000";
    public static final String DEFAULT_NAME = "DefaultTeam";

    private String id;
    private TeamName name;
    private Team parentTeam;
    private Person teamLeader;
    private final List<Person> members = new ArrayList<>();
    private final List<Team> subteams = new ArrayList<>();

    public TeamBuilder() {
        this.id = DEFAULT_ID;
        this.name = new TeamName(DEFAULT_NAME);
    }

    /**
     * Initializes the TeamBuilder with the data of {@code teamToCopy}.
     */
    public TeamBuilder(Team teamToCopy) {
        Objects.requireNonNull(teamToCopy);
        this.id = teamToCopy.getId();
        this.name = teamToCopy.getTeamName();
        this.parentTeam = teamToCopy.getParentTeam();
        this.teamLeader = teamToCopy.getLeaderPerson();
        this.members.clear();
        this.members.addAll(teamToCopy.getMembers());
        this.subteams.clear();
        this.subteams.addAll(teamToCopy.getSubteams());
    }

    public TeamBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public TeamBuilder withTeamName(TeamName teamName) {
        this.name = Objects.requireNonNull(teamName);
        return this;
    }

    public TeamBuilder withParentTeam(Team parent) {
        this.parentTeam = parent;
        return this;
    }

    public TeamBuilder withTeamLeader(Person leader) {
        this.teamLeader = leader;
        return this;
    }

    public TeamBuilder withMembers(Person... members) {
        this.members.clear();
        if (members != null) {
            this.members.addAll(Arrays.asList(members));
        }
        return this;
    }

    public TeamBuilder withSubteams(Team... subteams) {
        this.subteams.clear();
        if (subteams != null) {
            this.subteams.addAll(Arrays.asList(subteams));
        }
        return this;
    }

    public Team build() {
        Team team = new Team(id, name);
        if (parentTeam != null) {
            team.setParentTeam(parentTeam);
        }
        if (teamLeader != null) {
            team.withLeader(teamLeader);
        }
        if (!members.isEmpty()) {
            team.withMembers(new ArrayList<>(members));
        }
        if (!subteams.isEmpty()) {
            team.withSubteams(new ArrayList<>(subteams));
        }
        return team;
    }
}
