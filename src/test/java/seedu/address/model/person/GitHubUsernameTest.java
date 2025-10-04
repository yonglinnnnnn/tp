package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GitHubUsernameTest {
    private static final String VALID_GITHUB_USERNAME = "johndoe";
    private static final String INVALID_GITHUB_USERNAME = "-johnd*oe";


    @Test
    public void isValidGitHubUserName() {
        assertTrue(GitHubUsername.isValidGitHubUserName(VALID_GITHUB_USERNAME));
        assertFalse(GitHubUsername.isValidGitHubUserName(INVALID_GITHUB_USERNAME));
    }

    @Test
    public void value() {
        assertEquals(VALID_GITHUB_USERNAME, new GitHubUsername(VALID_GITHUB_USERNAME).value());
    }
}