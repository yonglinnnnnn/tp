package seedu.address.model.team;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import seedu.address.model.person.Person;

/**
 * Represents a team in the address book.
 *
 * Holds identity fields (id and {@link TeamName}) and relationships:
 * parent team, leader ({@link seedu.address.model.person.Person}), members and subteams.
 * Provides mutation helpers (add/remove/change) and compatibility accessors that return
 * ids for legacy code/tests.
 */
public class Team {
    private final String id;
    private final TeamName name;

    private Team parentTeam;
    private Person leaderPerson;
    private final List<Team> subTeams = new ArrayList<>();
    private final List<Person> members = new ArrayList<>();

    /**
     * Creates a Team from id and a name string.
     *
     * @param id team id; must not be null
     * @param name team name string; must not be null or empty
     */
    public Team(String id, String name) {
        this(id, new TeamName(name));
    }

    /**
     * Creates a Team from id and a {@link TeamName} object.
     *
     * @param id team id; must not be null
     * @param name {@link TeamName} instance; must not be null
     */
    public Team(String id, TeamName name) {
        requireNonNull(id);
        requireNonNull(name);
        this.id = id;
        this.name = name;
    }

    // Fluent constructor helpers if needed
    /**
     * Sets the parent team for this team and returns this instance.
     *
     * @param parent parent {@link Team}; must not be null
     * @return this team with the parent set
     * @throws NullPointerException if {@code parent} is null
     */
    public Team withParentTeam(Team parent) {
        requireNonNull(parent);
        this.parentTeam = parent;
        return this;
    }

    /**
     * Sets the leader person for this team and ensures the leader is in the members list.
     * Returns this team.
     *
     * @param leader the leader {@link Person}; may be null to clear
     * @return this team with the leader set
     */
    public Team withLeader(Person leader) {
        this.leaderPerson = leader;
        if (leader != null && !members.contains(leader)) {
            members.add(leader);
        }
        return this;
    }

    /**
     * Replaces this team's members with the provided list.
     *
     * @param membersList list of {@link Person} to set as members; must not be null
     * @return this team with members replaced
     * @throws NullPointerException if {@code membersList} is null
     */
    public Team withMembers(List<Person> membersList) {
        requireNonNull(membersList);
        this.members.clear();
        this.members.addAll(membersList);
        return this;
    }

    /**
     * Replaces this team's subteams with the provided list and sets their parent to this team.
     *
     * @param subteamList list of {@link Team} to set as subteams; must not be null
     * @return this team with subteams replaced
     * @throws NullPointerException if {@code subteamList} is null
     */
    public Team withSubteams(List<Team> subteamList) {
        requireNonNull(subteamList);
        this.subTeams.clear();
        this.subTeams.addAll(subteamList);
        subteamList.forEach(t -> t.setParentTeam(this));
        return this;
    }

    /**
     * Returns this team's id.
     *
     * @return team id string
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the {@link TeamName} of this team.
     *
     * @return {@link TeamName}
     */
    public TeamName getTeamName() {
        return name;
    }

    /**
     * Returns the parent {@link Team} or null if none.
     *
     * @return parent team or null
     */
    public Team getParentTeam() {
        return parentTeam;
    }

    /**
     * Sets the parent team reference for this team.
     *
     * @param parent the parent {@link Team}; may be null to clear
     */
    public void setParentTeam(Team parent) {
        this.parentTeam = parent;
    }

    /**
     * Returns the id of the parent team or null if no parent exists.
     *
     * @return parent team id or null
     */
    public String getParentTeamId() {
        return parentTeam == null ? null : parentTeam.getId();
    }

    /**
     * Returns the leader {@link Person} for this team, or null if none.
     *
     * @return leader person or null
     */
    public Person getLeaderPerson() {
        return leaderPerson;
    }

    /**
     * Changes the leader of this team to the provided {@link Person} and ensures they
     * are present in the members list.
     *
     * @param person the new leader; must not be null
     * @throws NullPointerException if {@code person} is null
     */
    public void changeLeader(Person person) {
        requireNonNull(person);
        if (!members.contains(person)) {
            members.add(person);
        }
        this.leaderPerson = person;
    }

    /**
     * Returns the id of the leader person, or null if no leader assigned.
     *
     * @return leader person id or null
     */
    public String getLeaderPersonId() {
        return leaderPerson == null ? null : leaderPerson.id();
    }

    /**
     * Adds a member to this team if not already present.
     *
     * @param person member to add; must not be null
     * @throws NullPointerException if {@code person} is null
     */
    public void addMember(Person person) {
        requireNonNull(person);
        if (!members.contains(person)) {
            members.add(person);
        }
    }

    /**
     * Removes a member from this team. If the removed member was the team leader, the leader is cleared.
     *
     * @param person member to remove; must not be null
     * @throws NullPointerException if {@code person} is null
     */
    public void removeMember(Person person) {
        requireNonNull(person);
        members.remove(person);
        if (Objects.equals(leaderPerson, person)) {
            leaderPerson = null;
        }
    }

    /**
     * Returns an unmodifiable list of this team's members.
     *
     * @return unmodifiable list of members
     */
    public List<Person> getMembers() {
        return Collections.unmodifiableList(members);
    }

    /**
     * Returns an unmodifiable list of member ids for compatibility with legacy code/tests.
     *
     * @return unmodifiable list of member id strings
     */
    public List<String> getMemberPersonIds() {
        List<String> ids = new ArrayList<>();
        for (Person p : members) {
            ids.add(p.id());
        }
        return Collections.unmodifiableList(ids);
    }

    /**
     * Adds a subteam to this team and sets this team as the parent of the subteam.
     *
     * @param subteam subteam to add; must not be null
     * @throws NullPointerException if {@code subteam} is null
     */
    public void addSubteam(Team subteam) {
        requireNonNull(subteam);
        if (!subTeams.contains(subteam)) {
            subTeams.add(subteam);
            subteam.setParentTeam(this);
        }
    }

    /**
     * Removes a subteam from this team and clears its parent reference.
     *
     * @param subteam subteam to remove; must not be null
     * @throws NullPointerException if {@code subteam} is null
     */
    public void removeSubteam(Team subteam) {
        requireNonNull(subteam);
        subTeams.remove(subteam);
        subteam.setParentTeam(null);
    }

    /**
     * Returns an unmodifiable list of subteams.
     *
     * @return unmodifiable list of subteams
     */
    public List<Team> getSubteams() {
        return Collections.unmodifiableList(subTeams);
    }

    /**
     * Returns an unmodifiable list of subteam ids for compatibility with legacy code/tests.
     *
     * @return unmodifiable list of subteam id strings
     */
    public List<String> getSubteamIds() {
        List<String> ids = new ArrayList<>();
        for (Team t : subTeams) {
            ids.add(t.getId());
        }
        return Collections.unmodifiableList(ids);
    }

    /**
     * Returns the team's display name (the underlying team name string).
     *
     * @return team name string
     */
    @Override
    public String toString() {
        return name.teamName();
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
        return id.equals(team.id)
                && name.equals(team.name)
                && Objects.equals(getParentTeamId(), team.getParentTeamId())
                && Objects.equals(getLeaderPersonId(), team.getLeaderPersonId())
                && getMemberPersonIds().equals(team.getMemberPersonIds())
                && getSubteamIds().equals(team.getSubteamIds());
    }

}
