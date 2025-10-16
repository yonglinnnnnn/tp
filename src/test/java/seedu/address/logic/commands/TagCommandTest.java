package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
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

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addSingleTagUnfilteredList_success() throws Exception {
        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("newTag"));

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

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
        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag1"));
        tagsToAdd.add(new Tag("tag2"));
        tagsToAdd.add(new Tag("tag3"));

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

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
    public void execute_addDuplicateTag_success() throws Exception {
        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

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

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        // Should succeed without error (idempotent operation)
        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_SUCCESS, Messages.format(personToTag));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("filteredTag"));

        Set<Tag> updatedTags = new HashSet<>(personInFilteredList.tags());
        updatedTags.addAll(tagsToAdd);
        Person taggedPerson = new PersonBuilder(personInFilteredList, true).withTags(
                updatedTags.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_SUCCESS, Messages.format(taggedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), taggedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag"));
        TagCommand tagCommand = new TagCommand(outOfBoundIndex, tagsToAdd);

        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag"));
        TagCommand tagCommand = new TagCommand(outOfBoundIndex, tagsToAdd);

        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        Set<Tag> tagsFirst = new HashSet<>();
        tagsFirst.add(new Tag("first"));
        Set<Tag> tagsSecond = new HashSet<>();
        tagsSecond.add(new Tag("second"));

        TagCommand tagFirstCommand = new TagCommand(INDEX_FIRST_PERSON, tagsFirst);
        TagCommand tagSecondCommand = new TagCommand(INDEX_SECOND_PERSON, tagsSecond);

        // same object -> returns true
        assertTrue(tagFirstCommand.equals(tagFirstCommand));

        // same values -> returns true
        TagCommand tagFirstCommandCopy = new TagCommand(INDEX_FIRST_PERSON, tagsFirst);
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
        Index index = Index.fromOneBased(1);
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend"));

        TagCommand tagCommand = new TagCommand(index, tags);
        String expected = TagCommand.class.getCanonicalName() + "{index=" + index + ", tagsToAdd=" + tags + "}";
        assertEquals(expected, tagCommand.toString());
    }
}
