package seedu.address.model.team;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.team.exceptions.InvalidSubteamNesting;

/**
 * Represents a Team in the address book.
 * Members are represented as a list of person IDs (String).
 * Leader is represented by a person ID (String).
 */
public class Team {
    private final String id;
    private final TeamName teamName;
    private List<String> members = new ArrayList<>();
    private String leaderId = null;
    private Subteams subteams = null;
    private String parentTeamId = null;

    /**
     * Constructs a Team with the given id and name.
     *
     * @param id       non-null unique identifier for the team
     * @param teamName non-null TeamName representing the team's name
     */
    public Team(String id, TeamName teamName) {
        requireNonNull(id);
        requireNonNull(teamName);
        this.id = id;
        this.teamName = teamName;
        this.subteams = new Subteams();
    }

    public String getId() {
        return id;
    }

    public TeamName getTeamName() {
        return teamName;
    }

    /**
     * Returns an unmodifiable view of the members (person IDs).
     */
    public List<String> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public String getLeaderId() {
        return leaderId;
    }

    public Subteams getSubteams() {
        return this.subteams;
    }

    /**
     * Adds a new subteam to this team.
     */
    public Team addToSubteam(String subteamId) throws InvalidSubteamNesting {
        requireNonNull(subteamId);
        if (subteamId.equals(this.id)) {
            throw new InvalidSubteamNesting();
        }
        // prevent cycles
        if (subteams.contains(subteamId)) {
            throw new InvalidSubteamNesting();
        } else {
            subteams.add(subteamId, this.id);
        }
        return this;
    }

    /**
     * Checks if a team with the given ID is in this team's nested subteams.
     *
     * @param teamId the ID of the team to check
     * @return true if the team is a subteam, false otherwise
     */
    public boolean containsTeamInSubteams(String teamId) {
        return subteams.contains(teamId);
    }

    /**
     * Checks if a team with the given ID is in this team's nested subteams.
     *
     * @param team the team to check
     * @return true if the team is a subteam, false otherwise
     */
    public boolean containsTeamInSubteams(Team team) {
        return subteams.contains(team.getId());
    }

    /**
     * Gets the parent team of this team.
     *
     * @return the parent team; null if this is a root level team
     */
    public String getParentTeamId() {
        return parentTeamId;
    }

    /**
     * Sets the parent team for this team.
     *
     * @param parentTeamId the parent team ID to set; null indicates root level team
     * @return this team with the parent set
     */
    public Team setParentTeamId(String parentTeamId) {
        this.parentTeamId = parentTeamId;
        return this;
    }

    /**
     * Fluent mutator: replace members list.
     *
     * @param newMembers list of new member IDs; must not be null
     */
    public Team withMembers(List<String> newMembers) {
        requireNonNull(newMembers);
        this.members = new ArrayList<>(newMembers);
        return this;
    }

    /**
     * Adds a member to this team if not already present.
     *
     * @param personId member to add; must not be null
     * @throws NullPointerException if {@code person} is null
     */
    public void addMember(String personId) {
        requireNonNull(personId);
        if (!members.contains(personId)) {
            members.add(personId);
        }
    }

    /**
     * Removes a member from this team. If the removed member was the team leader, the leader is cleared.
     *
     * @param personId member to remove; must not be null
     */
    public void removeMember(String personId) {
        requireNonNull(personId);
        members.remove(personId);
        if (Objects.equals(leaderId, personId)) {
            leaderId = null;
        }
    }

    /**
     * Changes the leader of this team to the provided {@link String} and ensures they
     * are present in the members list.
     *
     * @param personId the new leader; must not be null
     * @throws NullPointerException if {@code person} is null
     */
    public void changeLeader(String personId) {
        requireNonNull(personId);
        if (!members.contains(personId)) {
            members.add(personId);
        }
        this.leaderId = personId;
    }

    /**
     * Fluent mutator: set leader by person id.
     * Ensures leader is added to members and returns this for chaining.
     */
    public Team withLeader(String leaderId) {
        requireNonNull(leaderId);
        changeLeader(leaderId);
        return this;
    }

    /**
     * Replaces this team's subteams with the provided list and sets their parent to this team.
     *
     * @param subteams list of team IDs to set as subteams; must not be null
     * @return this team with subteams replaced
     */
    public Team withSubteams(Subteams subteams) {
        if (subteams != null) {
            this.subteams.setAll(subteams.getUnmodifiableList());
        }
        return this;
    }

    /**
     * Sets the parent team for this team and returns this instance.
     *
     * @param parentId parent {@link Team}; must not be null
     * @return this team with the parent set
     */
    public Team withParentTeamId(String parentId) {
        this.parentTeamId = parentId;
        return this;
    }

    /**
     * Compares this team to another team for a deep identity match used in tests.
     *
     * <p>Two teams are considered the same if they share id, name, parent id, leader id,
     * member ids and subteam ids.
     *
     * @param team other team to compare against; may be null
     * @return true if considered the same, false otherwise
     */
    public boolean isSameTeam(Team team) {
        if (team == this) {
            return true;
        }
        if (team == null) {
            return false;
        }
        return id.equals(team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamName, members, leaderId, subteams, parentTeamId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("teamName", teamName)
                .add("leaderId", leaderId)
                .add("members", members)
                .add("subteams", subteams)
                .add("parentTeam", parentTeamId)
                .toString();
    }
}
