package seedu.address.model.team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a modifiable list of subteams.
 * Provides utility methods to add, remove and query the contained teams.
 */
public class Subteams {

    private final List<Team> internalList = new ArrayList<>();

    /**
     * Creates an empty Subteams list.
     */
    public Subteams() {
    }

    /**
     * Creates a Subteams list containing the teams in the given collection.
     *
     * @param teams the collection whose teams are to be placed into this list
     */
    public Subteams(Collection<Team> teams) {
        Objects.requireNonNull(teams);
        internalList.addAll(teams);
    }

    /**
     * Adds a team to the subteams list.
     *
     * @param team the team to add
     * @throws NullPointerException if team is null
     */
    public void add(Team team) {
        Objects.requireNonNull(team);
        internalList.add(team);
    }

    /**
     * Removes a team from the subteams list.
     *
     * @param team the team to remove
     * @return true if the list contained the specified team
     * @throws NullPointerException if team is null
     */
    public boolean remove(Team team) {
        Objects.requireNonNull(team);
        return internalList.remove(team);
    }

    /**
     * Replaces the contents of this list with the given collection of teams.
     *
     * @param teams the collection of teams to set
     * @throws NullPointerException if teams is null
     */
    public void setAll(Collection<Team> teams) {
        Objects.requireNonNull(teams);
        internalList.clear();
        internalList.addAll(teams);
    }

    /**
     * Returns an unmodifiable view of the internal list.
     *
     * @return unmodifiable list of teams
     */
    public List<Team> getUnmodifiableList() {
        return Collections.unmodifiableList(internalList);
    }

    /**
     * Returns true if the list contains the given team.
     * This checks the top-level list and recursively checks subteams of contained teams.
     *
     * @param team the team to check
     * @return true if present in this list or any nested subteams
     */
    public boolean contains(Team team) {
        Objects.requireNonNull(team);
        for (Team t : internalList) {
            if (containsRecursive(t, team)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to perform DFS over subteams, avoiding cycles.
     * @param current the current team being checked
     * @param target the target team to find
     */
    private boolean containsRecursive(Team current, Team target) {
        if (current == null) {
            return false;
        }
        if (current.equals(target)) {
            return true;
        }
        List<Team> nestedSubteams = current.getSubteams();
        if (nestedSubteams.isEmpty()) {
            return false;
        }
        for (Team nst : nestedSubteams) {
            if (containsRecursive(nst, target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the number of teams in this list.
     *
     * @return size of list
     */
    public int size() {
        return internalList.size();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Subteams otherList)) {
            return false;
        }
        return internalList.equals(otherList.internalList);
    }

    @Override
    public String toString() {
        return "Subteams" + internalList;
    }
}
