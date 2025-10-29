package seedu.address.model.team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import seedu.address.model.AddressBook;
import seedu.address.model.team.exceptions.TeamNotFoundException;

/**
 * Represents a modifiable list of subteams.
 * Provides utility methods to add, remove and query the contained teams.
 */
public class Subteams {

    private static AddressBook addressBook = null;
    private final List<String> internalList = new ArrayList<>();

    /**
     * Creates an empty Subteams list.
     */
    public Subteams() {}

    /**
     * Creates a Subteams list containing the teams in the given collection.
     *
     * @param teams the collection whose teams are to be placed into this list
     */
    public Subteams(Collection<String> teams) {
        Objects.requireNonNull(teams);
        internalList.addAll(teams);
    }

    /**
     * Sets the AddressBook instance for team lookups.
     * This must be called before using methods that require team lookups.
     *
     * @param ab the AddressBook instance
     */
    public static void setAddressBook(AddressBook ab) {
        addressBook = ab;
    }

    /**
     * Adds a team to the subteams list.
     *
     * @param teamId the ID of the team to add
     * @param parentTeamId the ID of the parent team
     * @throws NullPointerException if team is null
     */
    public void add(String teamId, String parentTeamId) {
        Objects.requireNonNull(teamId);
        internalList.add(teamId);
        Team updatedTeam = addressBook.getTeamById(teamId);
        updatedTeam.setParentTeamId(parentTeamId);
    }

    /**
     * Removes a team from the subteams list.
     *
     * @param teamId the team to remove
     * @return true if the list contained the specified team
     * @throws NullPointerException if team is null
     */
    public boolean remove(String teamId) {
        Objects.requireNonNull(teamId);
        return internalList.remove(teamId);
    }

    /**
     * Replaces the contents of this list with the given collection of teams.
     *
     * @param teams the collection of teams to set
     * @throws NullPointerException if teams is null
     */
    public void setAll(Collection<String> teams) {
        Objects.requireNonNull(teams);
        internalList.clear();
        internalList.addAll(teams);
    }

    /**
     * Returns an unmodifiable view of the internal list.
     *
     * @return unmodifiable list of teams
     */
    public List<String> getUnmodifiableList() {
        return Collections.unmodifiableList(internalList);
    }

    /**
     * Returns true if the list contains the given team.
     * This checks the top-level list and recursively checks subteams of contained teams.
     *
     * @param teamId the team to check
     * @return true if present in this list or any nested subteams
     */
    public boolean contains(String teamId) throws TeamNotFoundException {
        assert(addressBook != null) : "AddressBook must be set for subteams before using contains method.";
        Objects.requireNonNull(teamId);
        // check first level
        if (internalList.contains(teamId)) {
            return true;
        }
        // check nested levels
        for (String t : internalList) {
            if (containsRecursive(t, teamId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to perform DFS over subteams, avoiding cycles.
     *
     * @param currentTeamId the current team being checked
     * @param targetTeamId the target team to find
     * @return true if targetTeamId is found in the subteams of currentTeamId
     */
    private boolean containsRecursive(String currentTeamId, String targetTeamId) throws TeamNotFoundException {
        assert(currentTeamId != null && targetTeamId != null) : "Team IDs must not be null.";
        if (currentTeamId.equals(targetTeamId)) {
            return true;
        }
        Team current = addressBook.getTeamById(currentTeamId);
        if (current == null) {
            throw new TeamNotFoundException();
        }
        List<String> nestedSubteamsList = current.getSubteams().getUnmodifiableList();
        if (nestedSubteamsList.isEmpty()) {
            return false;
        }
        for (String teamId : nestedSubteamsList) {
            if (teamId.equals(targetTeamId)) {
                return true;
            } else if (containsRecursive(teamId, targetTeamId)) {
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
