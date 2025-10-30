package seedu.address.model.team;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalTeams.CORE;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.team.exceptions.DuplicateTeamException;
import seedu.address.model.team.exceptions.TeamNotFoundException;
import seedu.address.testutil.TeamBuilder;

/**
 * Unit tests for {@link UniqueTeamList}.
 */
public class UniqueTeamListTest {

    @Test
    public void contains_teamWithSameId_returnsTrue() {
        UniqueTeamList list = new UniqueTeamList();
        list.add(CORE);
        // team with same id but different name should be considered duplicate / same
        Team edited = new TeamBuilder(CORE).withTeamName("DifferentName").build();
        assertTrue(list.contains(edited));
    }

    @Test
    public void add_duplicateTeam_throwsDuplicateTeamException() {
        UniqueTeamList list = new UniqueTeamList();
        list.add(CORE);
        Team duplicate = new TeamBuilder(CORE).build();
        assertThrows(DuplicateTeamException.class, () -> list.add(duplicate));
    }

    @Test
    public void remove_teamNotFound_throwsTeamNotFoundException() {
        UniqueTeamList list = new UniqueTeamList();
        assertThrows(TeamNotFoundException.class, () -> list.remove(CORE));
    }

    @Test
    public void setTeams_listWithDuplicates_throwsDuplicateTeamException() {
        UniqueTeamList list = new UniqueTeamList();
        assertThrows(DuplicateTeamException.class, () -> list.setTeams(Arrays.asList(CORE, CORE)));
    }

    @Test
    void getHierarchyString_singleRoot_noSubteams() {
        UniqueTeamList list = new UniqueTeamList();
        Team root = new Team("T1", new TeamName("Root"));
        list.add(root);

        String expected = "Root #T1 Members: []\n";
        assertEquals(expected, list.getHierarchyString());
    }

    @Test
    void getHierarchyString_deeplyNested_subteamsPrintedWithBranches() {
        AddressBook ab = new AddressBook();
        Subteams.setAddressBook(ab);
        UniqueTeamList list = new UniqueTeamList();

        Team a = new Team("T1", new TeamName("A"));
        Team b = new Team("T2", new TeamName("B"));
        Team c = new Team("T3", new TeamName("C"));
        ab.addTeam(a);
        ab.addTeam(b);
        ab.addTeam(c);

        // establish subteam relations
        a.addToSubteam(b.getId());
        b.addToSubteam(c.getId());

        // ensure parent pointers so roots are detected correctly
        b.setParentTeamId(a.getId());
        c.setParentTeamId(b.getId());

        // add in order
        list.add(a);
        list.add(b);
        list.add(c);

        String expected =
                """
                        A #T1 Members: []
                        └── B #T2 Members: []
                            └── C #T3 Members: []
                        """;

        assertEquals(expected, list.getHierarchyString());
    }

    @Test
    void getHierarchyString_multipleRoots_andTheirSubteams() {
        UniqueTeamList list = new UniqueTeamList();
        AddressBook ab = new AddressBook();
        Subteams.setAddressBook(ab);

        Team root1 = new Team("T1", new TeamName("Root1"));
        Team child1 = new Team("T2", new TeamName("Child1"));
        Team root2 = new Team("T3", new TeamName("Root2"));
        ab.addTeam(root1);
        ab.addTeam(child1);
        ab.addTeam(root2);

        // set relationships: Root1 -> Child1
        root1.addToSubteam(child1.getId());
        child1.setParentTeamId(root1.getId());

        list.add(root1);
        list.add(child1);
        list.add(root2);

        String expected =
                """
                        Root1 #T1 Members: []
                        └── Child1 #T2 Members: []
                        Root2 #T3 Members: []
                        """;

        assertEquals(expected, list.getHierarchyString());
    }
}
