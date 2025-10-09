package seedu.address.model.person;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GitHubUsernameTest {
    private static final String VALID_GITHUB_USERNAME = "johndoe";
    private static final String INVALID_GITHUB_USERNAME = "-johnd*oe";


    @Test
    public void isValidGitHubUserName() {
        assertTrue(GitHubUsername.isValidGitHubUsername(VALID_GITHUB_USERNAME));
        assertFalse(GitHubUsername.isValidGitHubUsername(INVALID_GITHUB_USERNAME));
    }

    @Test
    public void value() {
        assertEquals(VALID_GITHUB_USERNAME, new GitHubUsername(VALID_GITHUB_USERNAME).value());
    }
}
