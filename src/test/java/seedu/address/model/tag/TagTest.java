package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag names
        assertFalse(Tag.isValidTagName("")); // empty string
        assertFalse(Tag.isValidTagName("     ")); // spaces only
        assertFalse(Tag.isValidTagName("hello-")); // trailing hyphen
        assertFalse(Tag.isValidTagName("-hello")); // leading hyphen
        assertFalse(Tag.isValidTagName("hello--world")); // consecutive hyphens
        assertFalse(Tag.isValidTagName("hello world")); // space not allowed
        assertFalse(Tag.isValidTagName("tag@name")); // special characters
        assertFalse(Tag.isValidTagName("a123456789-1234567890")); // 21 characters (too long)

        // valid tag names
        assertTrue(Tag.isValidTagName("a")); // single character
        assertTrue(Tag.isValidTagName("hello")); // simple tag
        assertTrue(Tag.isValidTagName("hello-world")); // hyphenated
        assertTrue(Tag.isValidTagName("likes-boardgames")); // multiple words with hyphen
        assertTrue(Tag.isValidTagName("senior-dev-lead")); // multiple hyphens
        assertTrue(Tag.isValidTagName("tag123")); // alphanumeric
        assertTrue(Tag.isValidTagName("123tag")); // starts with number
        assertTrue(Tag.isValidTagName("a1234567890123456789")); // exactly 20 characters
    }

    @Test
    public void equals_caseInsensitive() {
        Tag tag1 = new Tag("boardgames");
        Tag tag2 = new Tag("Boardgames");
        Tag tag3 = new Tag("BOARDGAMES");

        // different cases -> returns true
        assertTrue(tag1.equals(tag2));
        assertTrue(tag1.equals(tag3));
        assertTrue(tag2.equals(tag3));
    }

    @Test
    public void hashCode_caseInsensitive() {
        Tag tag1 = new Tag("boardgames");
        Tag tag2 = new Tag("Boardgames");

        // same hash code for different cases
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

}
