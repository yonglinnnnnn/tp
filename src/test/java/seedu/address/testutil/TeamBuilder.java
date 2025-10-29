package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import seedu.address.model.team.Team;
import seedu.address.model.team.TeamName;

/**
 * A utility class to help with building {@link seedu.address.model.team.Team} objects in tests.
 */
public class TeamBuilder {

    public static final String DEFAULT_ID = "T0001";
    public static final String DEFAULT_NAME = "Core";

    private String id;
    private TeamName teamName;
    private List<String> members;
    private String leaderId;
    private List<Team> subteams;
    private String parentTeamId;

    /**
     * Creates a {@code TeamBuilder} with default values.
     */
    public TeamBuilder() {
        this.id = DEFAULT_ID;
        this.teamName = new TeamName(DEFAULT_NAME);
        this.members = new ArrayList<>();
        this.leaderId = null;
        this.subteams = new ArrayList<>();
        this.parentTeamId = null;
    }

    /**
     * Creates a {@code TeamBuilder} initialized with the data of {@code teamToCopy}.
     *
     * @param teamToCopy the team whose data will be copied into this builder
     */
    public TeamBuilder(Team teamToCopy) {
        Objects.requireNonNull(teamToCopy);
        this.id = teamToCopy.getId();
        this.teamName = teamToCopy.getTeamName();
        this.members = new ArrayList<>(teamToCopy.getMembers());
        this.leaderId = teamToCopy.getLeaderId();
        this.subteams = new ArrayList<>(teamToCopy.getSubteams());
        this.parentTeamId = teamToCopy.getParentTeamId();
    }

    /**
     * Sets the id for the team being built.
     *
     * @param id the id to set
     * @return this builder
     */
    public TeamBuilder withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the team name for the team being built.
     *
     * @param name the team name to set
     * @return this builder
     */
    public TeamBuilder withTeamName(String name) {
        this.teamName = new TeamName(name);
        return this;
    }

    /**
     * Sets the team name for the team being built.
     *
     * @param teamName the {@code TeamName} to set
     * @return this builder
     */
    public TeamBuilder withTeamName(TeamName teamName) {
        this.teamName = teamName;
        return this;
    }

    /**
     * Replace members list.
     *
     * @param memberIds the list of member ids to set
     * @return this builder
     */
    public TeamBuilder withMembers(List<String> memberIds) {
        this.members = new ArrayList<>(memberIds);
        return this;
    }

    /**
     * Convenience: set members from varargs.
     *
     * @param memberIds the member ids to set
     * @return this builder
     */
    public TeamBuilder withMembers(String... memberIds) {
        this.members = new ArrayList<>(Arrays.asList(memberIds));
        return this;
    }

    /**
     * Adds a single member id to the members list.
     *
     * @param memberId the member id to add
     * @return this builder
     */
    public TeamBuilder addMember(String memberId) {
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        this.members.add(memberId);
        return this;
    }

    /**
     * Sets the leader for the team being built.
     *
     * @param leaderId the leader id to set
     * @return this builder
     */
    public TeamBuilder withLeader(String leaderId) {
        this.leaderId = leaderId;
        return this;
    }

    /**
     * Sets the subteams list.
     *
     * @param subteams the list of subteams to set
     * @return this builder
     */
    public TeamBuilder withSubteams(List<Team> subteams) {
        this.subteams = new ArrayList<>(subteams);
        return this;
    }

    /**
     * Sets the subteams from varargs.
     *
     * @param subteams the subteams to set
     * @return this builder
     */
    public TeamBuilder withSubteams(Team... subteams) {
        this.subteams = new ArrayList<>(Arrays.asList(subteams));
        return this;
    }

    /**
     * Sets the parent team for the team being built.
     *
     * @param parentId the parent team to set
     * @return this builder
     */
    public TeamBuilder withParentTeamId(String parentId) {
        this.parentTeamId = parentId;
        return this;
    }

    /**
     * Builds a {@link Team} instance with the configured properties.
     *
     * @return the built Team
     */
    public Team build() {
        Team team = new Team(id, teamName);
        // apply members
        if (members != null) {
            team.withMembers(new ArrayList<>(members));
        }
        // apply leader (will add to members if absent)
        if (leaderId != null) {
            team.withLeader(leaderId);
        }
        // apply subteams and parent
        if (subteams != null) {
            team.withSubteams(new ArrayList<>(subteams));
        }
        if (parentTeamId != null) {
            team.withParentTeamId(parentTeamId);
        }
        return team;
    }
}
