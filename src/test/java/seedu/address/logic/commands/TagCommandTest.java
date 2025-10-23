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
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for TagCommand.
 */
public class TagCommandTest {
    private static final int INDEX_FIRST_PERSON = 0;
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addSingleTagUnfilteredList_success() throws Exception {
        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON);
        String employeeId = personToTag.id();
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("newTag"));

        TagCommand tagCommand = new TagCommand(employeeId, tagsToAdd);

        Set<Tag> updatedTags = new HashSet<>(personToTag.tags());
        updatedTags.addAll(tagsToAdd);
        Person taggedPerson = new PersonBuilder(personToTag, true).withTags(
                updatedTags.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_SUCCESS, Messages.format(taggedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToTag, taggedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addMultipleTagsUnfilteredList_success() throws Exception {
        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON);
        String employeeId = personToTag.id();
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag1"));
        tagsToAdd.add(new Tag("tag2"));
        tagsToAdd.add(new Tag("tag3"));

        TagCommand tagCommand = new TagCommand(employeeId, tagsToAdd);

        Set<Tag> updatedTags = new HashSet<>(personToTag.tags());
        updatedTags.addAll(tagsToAdd);
        Person taggedPerson = new PersonBuilder(personToTag, true).withTags(
                updatedTags.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_SUCCESS, Messages.format(taggedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToTag, taggedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addDuplicateTag_success() throws CommandException {
        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON);
        String employeeId = personToTag.id();

        // Get an existing tag from the person
        Set<Tag> existingTags = personToTag.tags();
        if (existingTags.isEmpty()) {
            // If no tags exist, add one first
            Set<Tag> initialTag = new HashSet<>();
            initialTag.add(new Tag("existingTag"));
            Person personWithTag = new PersonBuilder(personToTag, true)
                    .withTags("existingTag").build();
            model.setPerson(personToTag, personWithTag);
            personToTag = personWithTag;
        }

        // Try to add a tag that already exists
        Set<Tag> tagsToAdd = new HashSet<>(personToTag.tags());

        TagCommand tagCommand = new TagCommand(employeeId, tagsToAdd);

        // Should succeed without error (idempotent operation)
        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_SUCCESS, Messages.format(personToTag));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidEmployeeId_failure() throws Exception {
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag"));
        TagCommand tagCommand = new TagCommand("E9999", tagsToAdd);

        assertCommandFailure(tagCommand, model, String.format(TagCommand.MESSAGE_PERSON_NOT_FOUND, "E9999"));
    }

    @Test
    public void equals() throws Exception {
        Set<Tag> tagsFirst = new HashSet<>();
        tagsFirst.add(new Tag("first"));
        Set<Tag> tagsSecond = new HashSet<>();
        tagsSecond.add(new Tag("second"));

        TagCommand tagFirstCommand = new TagCommand("E1001", tagsFirst);
        TagCommand tagSecondCommand = new TagCommand("E1002", tagsSecond);

        // same object -> returns true
        assertTrue(tagFirstCommand.equals(tagFirstCommand));

        // same values -> returns true
        TagCommand tagFirstCommandCopy = new TagCommand("E1001", tagsFirst);
        assertTrue(tagFirstCommand.equals(tagFirstCommandCopy));

        // different types -> returns false
        assertFalse(tagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(tagFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(tagFirstCommand.equals(tagSecondCommand));
    }

    @Test
    public void toStringMethod() throws Exception {
        String employeeId = "E1001";
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend"));

        TagCommand tagCommand = new TagCommand(employeeId, tags);
        String expected = TagCommand.class.getCanonicalName()
                + "{employeeId=" + employeeId + ", tagsToAdd=" + tags + "}";
        assertEquals(expected, tagCommand.toString());
    }
}
