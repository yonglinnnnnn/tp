package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UntagCommand.
 */
public class UntagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_removeSingleTagUnfilteredList_success() throws Exception {
        Person personToUntag = model.getFilteredPersonList().get(0);
        String employeeId = personToUntag.id();

        // First ensure the person has tags
        if (personToUntag.tags().isEmpty()) {
            Set<Tag> initialTags = new HashSet<>();
            initialTags.add(new Tag("tagToRemove"));
            Person personWithTag = new PersonBuilder(personToUntag, true)
                    .withTags("tagToRemove").build();
            model.setPerson(personToUntag, personWithTag);
            personToUntag = personWithTag;
        }

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(personToUntag.tags().iterator().next());

        UntagCommand untagCommand = new UntagCommand(employeeId, tagsToRemove);

        Set<Tag> updatedTags = new HashSet<>(personToUntag.tags());
        updatedTags.removeAll(tagsToRemove);
        Person untaggedPerson = new PersonBuilder(personToUntag, true).withTags(
                updatedTags.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();

        String expectedMessage = String.format(UntagCommand.MESSAGE_UNTAG_SUCCESS, Messages.format(untaggedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToUntag, untaggedPerson);

        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_removeMultipleTagsUnfilteredList_success() throws Exception {
        Person personToUntag = model.getFilteredPersonList().get(0);
        String employeeId = personToUntag.id();

        // Add multiple tags first
        Person personWithTags = new PersonBuilder(personToUntag, true)
                .withTags("tag1", "tag2", "tag3").build();
        model.setPerson(personToUntag, personWithTags);
        personToUntag = personWithTags;

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("tag1"));
        tagsToRemove.add(new Tag("tag2"));

        UntagCommand untagCommand = new UntagCommand(employeeId, tagsToRemove);

        Set<Tag> updatedTags = new HashSet<>(personToUntag.tags());
        updatedTags.removeAll(tagsToRemove);
        Person untaggedPerson = new PersonBuilder(personToUntag, true).withTags(
                updatedTags.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();

        String expectedMessage = String.format(UntagCommand.MESSAGE_UNTAG_SUCCESS, Messages.format(untaggedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToUntag, untaggedPerson);

        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_removeNonExistentTag_failure() throws Exception {
        Person personToUntag = model.getFilteredPersonList().get(0);
        String employeeId = personToUntag.id();

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("nonExistentTag"));

        UntagCommand untagCommand = new UntagCommand(employeeId, tagsToRemove);

        assertCommandFailure(untagCommand, model, String.format(UntagCommand.MESSAGE_TAG_NOT_FOUND, tagsToRemove));
    }

    @Test
    public void execute_invalidEmployeeId_failure() throws Exception {
        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("tag"));
        UntagCommand untagCommand = new UntagCommand("E9999", tagsToRemove);

        assertCommandFailure(untagCommand, model, String.format(UntagCommand.MESSAGE_PERSON_NOT_FOUND, "E9999"));
    }

    @Test
    public void equals() throws Exception {
        Set<Tag> tagsFirst = new HashSet<>();
        tagsFirst.add(new Tag("first"));
        Set<Tag> tagsSecond = new HashSet<>();
        tagsSecond.add(new Tag("second"));

        UntagCommand untagFirstCommand = new UntagCommand("E1001", tagsFirst);
        UntagCommand untagSecondCommand = new UntagCommand("E1002", tagsSecond);

        // same object -> returns true
        assertTrue(untagFirstCommand.equals(untagFirstCommand));

        // same values -> returns true
        UntagCommand untagFirstCommandCopy = new UntagCommand("E1001", tagsFirst);
        assertTrue(untagFirstCommand.equals(untagFirstCommandCopy));

        // different types -> returns false
        assertFalse(untagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(untagFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(untagFirstCommand.equals(untagSecondCommand));
    }

    @Test
    public void toStringMethod() throws Exception {
        String employeeId = "E1001";
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend"));
        UntagCommand untagCommand = new UntagCommand(employeeId, tags);
        String expected = UntagCommand.class.getCanonicalName()
                + "{employeeId=" + employeeId + ", tagsToRemove=" + tags + "}";
        assertEquals(expected, untagCommand.toString());
    }
}
