package seedu.address.model.team;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Manager that holds and manages a collection of {@link Team} objects.
 *
 * <p>Provides basic mutating operations (add, remove, replace), lookup by id and
 * an unmodifiable view of the stored teams. Designed for use in tests and
 * higher-level components that coordinate team operations.
 */
public class TeamsManager {
    private final List<Team> teams = new ArrayList<>();

    public TeamsManager() {}

    /**
     * Adds the given team if it is not already present.
     *
     * @param team the team to add (must not be null)
     */
    public void addTeam(Team team) {
        requireNonNull(team);
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    /**
     * Removes the given team if present.
     *
     * @param team the team to remove (must not be null)
     */
    public void removeTeam(Team team) {
        requireNonNull(team);
        teams.remove(team);
    }

    /**
     * Replaces {@code target} with {@code editedTeam}.
     *
     * @throws IllegalArgumentException if {@code target} is not present.
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
     * Returns true if an equivalent team exists in the manager.
     */
    public boolean hasTeam(Team team) {
        requireNonNull(team);
        return teams.contains(team);
    }

    /**
     * Finds a team by id.
     *
     * @param id the team id to search for (must not be null)
     * @return an Optional containing the team if found, otherwise empty
     */
    public Optional<Team> getTeamById(String id) {
        requireNonNull(id);
        return teams.stream().filter(team -> id.equals(team.getId())).findFirst();
    }

    /**
     * Returns an unmodifiable view of the teams held by this manager.
     */
    public List<Team> getTeamList() {
        return Collections.unmodifiableList(teams);
    }

    /**
     * Removes all teams from this manager.
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
        TeamsManager teamManager = (TeamsManager) other;
        return teams.equals(teamManager.teams);
    }

    @Override
    public String toString() {
        return "TeamsManager" + teams;
    }
}
