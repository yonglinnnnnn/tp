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
import seedu.address.model.team.exceptions.InvalidSubteamNesting;
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

    @Test
    public void addToSubteam_null_throwsNullPointerException() {
        Team t = new Team("T1004", new TeamName("SubteamTest"));
        assertThrows(NullPointerException.class, () -> t.addToSubteam(null));
    }

    @Test
    public void addToSubteam_addingSelf_throwsInvalidSubteamNestingException() {
        Team t = new Team("T2000", new TeamName("TestTeam"));
        assertThrows(InvalidSubteamNesting.class, () -> t.addToSubteam(t));
    }

    @Test
    public void addToSubteam_cyclicTeams_throwsInvalidSubteamNestingException() {
        Team teamA = new Team("T0001", new TeamName("A"));
        Team teamB = new Team("T0003", new TeamName("B"));
        assertThrows(InvalidSubteamNesting.class, () -> {
            Team at = teamA.addToSubteam(teamB);
            at.addToSubteam(teamB);
        });
        assertThrows(InvalidSubteamNesting.class, () -> {
            teamB.addToSubteam(teamA);
            teamA.addToSubteam(teamB);
        });
    }

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
