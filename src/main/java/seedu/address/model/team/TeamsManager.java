// java
package seedu.address.model.team;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Manager for Team objects.
 */
public class TeamsManager {
    private final List<Team> teams = new ArrayList<>();

    public TeamsManager() {}

    /**
     * Adds a team if it does not already exist (by Team.equals).
     */
    public void addTeam(Team team) {
        requireNonNull(team);
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    /**
     * Removes the given team if present.
     */
    public void removeTeam(Team team) {
        requireNonNull(team);
        teams.remove(team);
    }

    /**
     * Replaces target team with editedTeam. Throws IllegalArgumentException if target not found.
     */
    public void setTeam(Team target, Team editedTeam) {
        requireNonNull(target);
        requireNonNull(editedTeam);
        int index = teams.indexOf(target);
        if (index == -1) {
            throw new IllegalArgumentException("Target team not found");
        }
        teams.set(index, editedTeam);
    }

    /**
     * Returns true if the manager contains an equivalent team.
     */
    public boolean hasTeam(Team team) {
        requireNonNull(team);
        return teams.contains(team);
    }

    /**
     * Finds a team by id.
     */
    public Optional<Team> getTeamById(String id) {
        requireNonNull(id);
        return teams.stream().filter(team -> id.equals(team.getId())).findFirst();
    }

    /**
     * Returns an unmodifiable view of the team list.
     */
    public List<Team> getTeamList() {
        return Collections.unmodifiableList(teams);
    }

    /**
     * Deletes all teams.
     */
    public void resetTeams() {
        teams.clear();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TeamsManager)) {
            return false;
        }
        TeamsManager otherManager = (TeamsManager) other;
        return teams.equals(otherManager.teams);
    }

    @Override
    public String toString() {
        return "TeamsManager" + teams;
    }
}
