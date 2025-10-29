package seedu.address.model.team;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.team.exceptions.DuplicateTeamException;
import seedu.address.model.team.exceptions.InvalidSubteamNesting;
import seedu.address.model.team.exceptions.TeamNotFoundException;

/**
 * A list of teams that enforces uniqueness between its elements and does not allow nulls.
 * Identity is checked using Team#isSameTeam(Team).
 */
public class UniqueTeamList implements Iterable<Team> {

    private final ObservableList<Team> internalList = FXCollections.observableArrayList();
    private final ObservableList<Team> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent team as the given argument.
     */
    public boolean contains(Team toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameTeam);
    }

    /**
     * Returns the team with the given teamId.
     * Throws TeamNotFoundException if no such team is found.
     */
    public Team getTeamById(String teamId) {
        requireNonNull(teamId);
        for (Team team : internalList) {
            if (team.getId().equals(teamId)) {
                return team;
            }
        }
        throw new TeamNotFoundException();
    }

    /**
     * Adds a team to the list.
     * The team must not already exist in the list.
     */
    public void add(Team toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateTeamException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the team {@code target} in the list with {@code editedTeam}.
     * {@code target} must exist in the list.
     * The team identity of {@code editedTeam} must not be the same as another existing team in the list.
     */
    public void setTeam(Team target, Team editedTeam) throws TeamNotFoundException {
        requireNonNull(target);
        requireNonNull(editedTeam);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TeamNotFoundException();
        }

        if (!target.isSameTeam(editedTeam) && contains(editedTeam)) {
            throw new DuplicateTeamException();
        }

        internalList.set(index, editedTeam);
    }

    /**
     * Removes the equivalent team from the list.
     * The team must exist in the list.
     */
    public void remove(Team toRemove) throws TeamNotFoundException {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new TeamNotFoundException();
        }
    }

    /**
     * Replaces the contents of this list with {@code teams}.
     * {@code teams} must not contain duplicate teams.
     */
    public void setTeams(List<Team> teams) throws DuplicateTeamException {
        requireNonNull(teams);
        if (!teamsAreUnique(teams)) {
            throw new DuplicateTeamException();
        }
        internalList.setAll(teams);
    }

    /**
     * Adds a subteam to a parent team in the list.
     * The parent team must exist in the list.
     * The subteam must not already exist as a subteam of the parent team.
     *
     * @return true if the subteam was added successfully, false if adding the subteam would create invalid nesting
     */
    public boolean setSubteam(Team parentTeam, Team subteam) {
        requireNonNull(parentTeam);
        requireNonNull(subteam);

        int index = internalList.indexOf(parentTeam);
        if (index == -1) {
            throw new TeamNotFoundException();
        }
        try {
            subteam.setParentTeamId(parentTeam.getId());
            Team newTeam = parentTeam.addToSubteam(subteam.getId());
            internalList.set(index, newTeam);
        } catch (InvalidSubteamNesting e) {
            return false;
        }
        return true;
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Team> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Team> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UniqueTeamList otherUniqueTeamList)) {
            return false;
        }
        return internalList.equals(otherUniqueTeamList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Helper method to build the hierarchy string recursively.
     *
     * @return String representation of all the teams in Linux tree format.
     */
    public String getHierarchyString() {
        //    StringBuilder sb = new StringBuilder();
        //    // Start the tree with the root team
        //    sb.append(this.name).append("\n");
        //    // Call the recursive helper
        //    generateHierarchyTree(this.subTeams, sb, "", false);
        //    return sb.toString();
        return String.valueOf(internalList.size());
    }

    //    /**
    //     * Recursive helper method to build the tree structure.
    //     *
    //     * @param teams The list of subteams to process.
    //     * @param sb The StringBuilder to append the tree structure to.
    //     * @param prefix The current line prefix (e.g., "│   ").
    //     * @param isLast A flag indicating if the current team list is the last child of its parent.
    //     */
    //    private void generateHierarchyTree(StringBuilder sb, String prefix, boolean isLast) {
    //        for (int i = 0; i < internalList.size(); i++) {
    //            Team team = internalList.get(i);
    //            boolean isTeamLast = (i == internalList.size() - 1);
    //
    //            // 1. Append the appropriate branch symbol and team name
    //            sb.append(prefix);
    //            if (isTeamLast) {
    //                sb.append("└── "); // Last child in the list
    //            } else {
    //                sb.append("├── "); // Not the last child
    //            }
    //            sb.append(team.getName()).append("\n");
    //
    //            // 2. Calculate the new prefix for the sub-teams
    //            String newPrefix;
    //            if (isTeamLast) {
    //                // If it's the last team, the next prefix is just spaces (no vertical line)
    //                newPrefix = prefix + "    ";
    //            } else {
    //                // If not the last, continue the vertical line
    //                newPrefix = prefix + "│   ";
    //            }
    //
    //            // 3. Recurse for the team's sub-teams
    //            if (!team.getSubTeams().isEmpty()) {
    //                generateHierarchyTree(team.getSubTeams(), sb, newPrefix, isTeamLast);
    //            }
    //        }
    //    }

    /**
     * Returns true if {@code teams} contains only unique teams.
     */
    private boolean teamsAreUnique(List<Team> teams) {
        for (int i = 0; i < teams.size() - 1; i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                if (teams.get(i).isSameTeam(teams.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
