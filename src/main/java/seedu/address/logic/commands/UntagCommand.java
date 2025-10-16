package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Removes tags from an existing person in the address book.
 */
public class UntagCommand extends Command {

    public static final String COMMAND_WORD = "untag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes tags from the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) TAG [MORE_TAGS]...\n"
            + "Example: " + COMMAND_WORD + " 1 friends colleagues";

    public static final String MESSAGE_UNTAG_SUCCESS = "Removed tags from Person: %1$s";
    public static final String MESSAGE_NO_TAGS_PROVIDED = "At least one tag must be provided.";
    public static final String MESSAGE_TAG_NOT_FOUND = "Some tags were not found on this person: %1$s";

    private final Index index;
    private final Set<Tag> tagsToRemove;

    /**
     * @param index of the person in the filtered person list to remove tags from
     * @param tagsToRemove tags to remove from the person
     */
    public UntagCommand(Index index, Set<Tag> tagsToRemove) {
        requireNonNull(index);
        requireNonNull(tagsToRemove);

        this.index = index;
        this.tagsToRemove = tagsToRemove;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToUntag = lastShownList.get(index.getZeroBased());
        Person untaggedPerson = createUntaggedPerson(personToUntag, tagsToRemove);

        model.setPerson(personToUntag, untaggedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_UNTAG_SUCCESS, Messages.format(untaggedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the tags of {@code personToUntag}
     * with {@code tagsToRemove} removed.
     */
    private static Person createUntaggedPerson(Person personToUntag, Set<Tag> tagsToRemove) {
        assert personToUntag != null;

        Set<Tag> updatedTags = new HashSet<>(personToUntag.tags());
        updatedTags.removeAll(tagsToRemove);

        return new Person(
            personToUntag.id(),
            personToUntag.name(),
            personToUntag.phone(),
            personToUntag.email(),
            personToUntag.address(),
            personToUntag.gitHubUsername(),
            updatedTags
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UntagCommand)) {
            return false;
        }

        UntagCommand otherUntagCommand = (UntagCommand) other;
        return index.equals(otherUntagCommand.index)
                && tagsToRemove.equals(otherUntagCommand.tagsToRemove);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tagsToRemove", tagsToRemove)
                .toString();
    }
}

