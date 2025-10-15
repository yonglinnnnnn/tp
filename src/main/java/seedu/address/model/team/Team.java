package seedu.address.model.team;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import seedu.address.model.person.Person;

/**
 * Team model.
 */
public class Team {
    private final String id;
    private final TeamName name;

    private Team parentTeam;
    private Person leaderPerson;
    private final List<Team> subTeams = new ArrayList<>();
    private final List<Person> members = new ArrayList<>();

    public Team(String id, String name) {
        this(id, new TeamName(name));
    }

    public Team(String id, TeamName name) {
        requireNonNull(id);
        requireNonNull(name);
        this.id = id;
        this.name = name;
    }

    // Fluent constructor helpers if needed
    public Team withParentTeam(Team parent) {
        this.parentTeam = parent;
        return this;
    }

    public Team withLeader(Person leader) {
        this.leaderPerson = leader;
        if (leader != null && !members.contains(leader)) {
            members.add(leader);
        }
        return this;
    }

    public Team withMembers(List<Person> membersList) {
        requireNonNull(membersList);
        this.members.clear();
        this.members.addAll(membersList);
        return this;
    }

    public Team withSubteams(List<Team> subteamList) {
        requireNonNull(subteamList);
        this.subTeams.clear();
        this.subTeams.addAll(subteamList);
        subteamList.forEach(t -> t.setParentTeam(this));
        return this;
    }

    public String getId() {
        return id;
    }

    /**
     * Returns the TeamName object if callers need the richer type.
     */
    public TeamName getTeamName() {
        return name;
    }

    public Team getParentTeam() {
        return parentTeam;
    }

    public void setParentTeam(Team parent) {
        this.parentTeam = parent;
    }

    /**
     * Compatibility helper: returns parent team id or null if no parent.
     */
    public String getParentTeamId() {
        return parentTeam == null ? null : parentTeam.getId();
    }

    public Person getLeaderPerson() {
        return leaderPerson;
    }

    public void changeLeader(Person person) {
        requireNonNull(person);
        if (!members.contains(person)) {
            members.add(person);
        }
        this.leaderPerson = person;
    }

    /**
     * Compatibility helper: returns leader person id or null if no leader.
     */
    public String getLeaderPersonId() {
        return leaderPerson == null ? null : leaderPerson.id();
    }

    public void addMember(Person person) {
        requireNonNull(person);
        if (!members.contains(person)) {
            members.add(person);
        }
    }

    public void removeMember(Person person) {
        requireNonNull(person);
        members.remove(person);
        if (Objects.equals(leaderPerson, person)) {
            leaderPerson = null;
        }
    }

    public List<Person> getMembers() {
        return Collections.unmodifiableList(members);
    }

    /**
     * Compatibility helper: returns list of member ids.
     */
    public List<String> getMemberPersonIds() {
        List<String> ids = new ArrayList<>();
        for (Person p : members) {
            ids.add(p.id());
        }
        return Collections.unmodifiableList(ids);
    }

    public void addSubteam(Team subteam) {
        requireNonNull(subteam);
        if (!subTeams.contains(subteam)) {
            subTeams.add(subteam);
            subteam.setParentTeam(this);
        }
    }

    public void removeSubteam(Team subteam) {
        requireNonNull(subteam);
        subTeams.remove(subteam);
        if (subteam != null) {
            subteam.setParentTeam(null);
        }
    }

    public List<Team> getSubteams() {
        return Collections.unmodifiableList(subTeams);
    }

    public List<String> getSubteamIds() {
        List<String> ids = new ArrayList<>();
        for (Team t : subTeams) {
            ids.add(t.getId());
        }
        return Collections.unmodifiableList(ids);
    }

    @Override
    public String toString() {
        return name.teamName();
    }

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
