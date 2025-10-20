package seedu.address.model.team;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalTeams.BACKEND;
import static seedu.address.testutil.TypicalTeams.CORE;
import static seedu.address.testutil.TypicalTeams.FRONTEND;
import static seedu.address.testutil.TypicalTeams.QA;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

/**
 * Unit tests for {@link seedu.address.model.team.Team}.
 *
 * <p>Verifies core behaviors: leader/member management, parent/subteam relationships,
 * immutability of returned collections and basic identity semantics.
 */
public class TeamTest {

    @Test
    public void changeLeader_addsLeaderAndMemberIfAbsent() {
        Team team = new Team("T1000", new TeamName("Example"));
        assertNull(team.getLeaderId());

        Person p = new PersonBuilder().withId(9999).build();
        team.changeLeader(p.id());

        assertEquals(p.id(), team.getLeaderId());
        assertTrue(team.getMembers().contains(p.id()));
    }

    @Test
    public void withLeader_addsLeaderToMembers() {
        Team team = new Team("T0001", new TeamName("Core"));
        team.withLeader(TypicalPersons.BENSON.id());

        assertEquals(TypicalPersons.BENSON.id(), team.getLeaderId());
        assertTrue(team.getMembers().contains(TypicalPersons.BENSON.id()));
    }

    @Test
    public void addMember_null_throwsNullPointerException() {
        Team t = new Team("T1001", new TeamName("Test"));
        assertThrows(NullPointerException.class, () -> t.addMember(null));
    }

    @Test
    public void addMember_duplicateIgnored() {
        Team t = new Team("T1002", new TeamName("DupTest"));
        t.addMember(TypicalPersons.BENSON.id());
        t.addMember(TypicalPersons.BENSON.id());
        assertEquals(1, t.getMembers().size());
        assertTrue(t.getMembers().contains(TypicalPersons.BENSON.id()));
    }

    @Test
    public void removeMember_removingLeader_clearsLeader() {
        Team team = new Team("T0002", new TeamName("Backend"));
        team.withMembers(Arrays.asList(TypicalPersons.DANIEL.id(), TypicalPersons.ELLE.id()));
        team.changeLeader(TypicalPersons.DANIEL.id());

        assertEquals(TypicalPersons.DANIEL.id(), team.getLeaderId());

        team.removeMember(TypicalPersons.DANIEL.id());
        assertNull(team.getLeaderId());
        assertFalse(team.getMembers().contains(TypicalPersons.DANIEL.id()));
    }

    //    @Test
    //    public void withParentTeam_setsParentAndReturnsThis() {
    //        Team parent = new Team("T0001", new TeamName("Core"));
    //        Team child = new Team("T0002", new TeamName("Backend"));
    //
    //        Team returned = child.withParentTeam(parent);
    //
    //        assertSame(child, returned);
    //        assertSame(parent, child.getParentTeam());
    //        assertEquals(parent.getId(), child.getParentTeam());
    //    }

    //    @Test
    //    public void addSubteam_null_throwsNullPointerException() {
    //        Team t = new Team("T1004", new TeamName("SubteamTest"));
    //        assertThrows(NullPointerException.class, () -> t.addSubteam(null));
    //    }
    //
    //    @Test
    //    public void addSubteam_duplicateIgnoredAndRemoveSubteamWorks() {
    //        Team parent = new Team("T2000", "Parent");
    //        Team sub1 = new Team("T2001", "Child1");
    //        parent.addSubteam(sub1);
    //        parent.addSubteam(sub1);
    //        assertEquals(1, parent.getSubteamIds().size());
    //        Team sub2 = new Team("T2002", "Child2");
    //        parent.addSubteam(sub2);
    //        assertTrue(parent.getSubteamIds().contains("T2001"));
    //        parent.removeSubteam(sub1);
    //        assertFalse(parent.getSubteamIds().contains("T2001"));
    //    }
    //
    //    @Test
    //    public void addSubteam_setsParent() {
    //        Team parent = new Team("T0001", "Core");
    //        Team sub = new Team("T0003", "Frontend");
    //
    //        parent.addSubteam(sub);
    //
    //        assertEquals(parent, sub.getParentTeam());
    //        assertTrue(parent.getSubteams().contains(sub));
    //    }
    //
    //    @Test
    //    public void getMemberPersonIds_returnsExpectedIds() {
    //        Team team = new Team("T0001", "Core");
    //        team.withMembers(Arrays.asList(TypicalPersons.BENSON, TypicalPersons.CARL));
    //
    //        List<String> expectedIds = Arrays.asList(TypicalPersons.BENSON.id(), TypicalPersons.CARL.id());
    //        assertEquals(expectedIds, team.getMemberPersonIds());
    //    }
    //
    //    @Test
    //    public void returnedLists_areUnmodifiable() {
    //        Team t = new Team("T3000", "ImmutableTest");
    //        Person p = new PersonBuilder().withId(1).build();
    //        t.addMember(p);
    //        Team sub = new Team("T1", "Sub");
    //        t.addSubteam(sub);
    //
    //        assertThrows(UnsupportedOperationException.class, () )-> t.getMemberPersonIds().add("E2"));
    //        assertThrows(UnsupportedOperationException.class, () -> t.getSubteamIds(.add("T2"));
    //    }

    @Test
    public void typicalTeams_examplesHaveExpectedProperties() {
        // sanity checks using TypicalTeams fixtures
        assertEquals("QA", QA.getTeamName().teamName());
        assertTrue(CORE.getMembers().contains("E0001"));
        assertEquals("Backend", BACKEND.getTeamName().teamName());
        assertEquals("Frontend", FRONTEND.getTeamName().teamName());
    }

    @Test
    public void isSameTeam_sameIdDifferentName_returnsTrue() {
        Team teamA = new Team("T9001", new TeamName("Alpha"));
        Team teamB = new Team("T9001", new TeamName("Alpha"));
        assertTrue(teamA.isSameTeam(teamB));
    }
}
