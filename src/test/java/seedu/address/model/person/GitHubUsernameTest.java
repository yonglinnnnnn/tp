package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

class GitHubUsernameTest {

    @Test
    public void isValidGitHubUsername() {
        // null username
        assertThrows(NullPointerException.class, () -> GitHubUsername.isValidGitHubUsername(null));

        // invalid username
        assertFalse(GitHubUsername.isValidGitHubUsername("^")); // only non-alphanumeric characters
        assertFalse(GitHubUsername.isValidGitHubUsername("@peter*")); // contains non-alphanumeric characters
        assertFalse(GitHubUsername.isValidGitHubUsername("peter01")); // does not start with '@'
        assertFalse(GitHubUsername.isValidGitHubUsername("@-peter01")); // starts with hyphen
        assertFalse(GitHubUsername.isValidGitHubUsername("@peter01-")); // ends with hyphen
        assertFalse(GitHubUsername.isValidGitHubUsername("@peter--01")); // multiple consecutive hyphens
        assertFalse(GitHubUsername.isValidGitHubUsername("@pe")); // shorter than 3 characters
        assertFalse(GitHubUsername.isValidGitHubUsername("@ThisIsAVeryVeryVeryVeryVeryLongGitHubUsername")); //
        // longer than 39 characters


        // valid username
        assertTrue(GitHubUsername.isValidGitHubUsername("")); // empty string
        assertTrue(GitHubUsername.isValidGitHubUsername(" ")); // spaces only
        assertTrue(GitHubUsername.isValidGitHubUsername("@peterjack")); // alphabets only
        assertTrue(GitHubUsername.isValidGitHubUsername("@12345")); // numbers only
        assertTrue(GitHubUsername.isValidGitHubUsername("@peterthe2nd")); // alphanumeric characters
        assertTrue(GitHubUsername.isValidGitHubUsername("@CapitalTan")); // with capital letters
        assertTrue(GitHubUsername.isValidGitHubUsername("@peter-the-2nd")); // with hyphens
    }

    @Test
    public void equals() {
        GitHubUsername username = new GitHubUsername("@ValidGitHubUsername");

        // same values -> returns true
        assertTrue(username.equals(new GitHubUsername("@ValidGitHubUsername")));

        // same object -> returns true
        assertTrue(username.equals(username));

        // null -> returns false
        assertFalse(username.equals(null));

        // different types -> returns false
        assertFalse(username.equals(5.0f));

        // different values -> returns false
        assertFalse(username.equals(new GitHubUsername("@OtherValidGitHubUsername")));
    }
}
