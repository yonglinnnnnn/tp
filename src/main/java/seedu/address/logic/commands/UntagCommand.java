package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            + "by their employee ID.\n"
            + "Parameters: EMPLOYEE_ID (must start with E) TAG [MORE_TAGS]...\n"
            + "Example: " + COMMAND_WORD + " E1001 friends colleagues";

    public static final String MESSAGE_UNTAG_SUCCESS = "Removed tags from Person: %1$s";
    public static final String MESSAGE_NO_TAGS_PROVIDED = "At least one tag must be provided.";
    public static final String MESSAGE_TAG_NOT_FOUND = "Some tags were not found on this person: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No person with employee ID %1$s found.";

    private final String employeeId;
    private final Set<Tag> tagsToRemove;

    /**
     * @param employeeId of the person to remove tags from
     * @param tagsToRemove tags to remove from the person
     */
    public UntagCommand(String employeeId, Set<Tag> tagsToRemove) {
        requireNonNull(employeeId);
        requireNonNull(tagsToRemove);

        this.employeeId = employeeId;
        this.tagsToRemove = tagsToRemove;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Person personToUntag = lastShownList.stream()
                .filter(person -> person.id().equals(employeeId))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, employeeId)));

        // Validate that all tags to remove exist on the person
        Set<Tag> personTags = personToUntag.tags();
        Set<Tag> nonExistentTags = new HashSet<>();
        for (Tag tag : tagsToRemove) {
            if (!personTags.contains(tag)) {
                nonExistentTags.add(tag);
            }
        }

        if (!nonExistentTags.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_TAG_NOT_FOUND, nonExistentTags));
        }

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

        return personToUntag.duplicate(personToUntag.id())
                .withTags(updatedTags)
                .build();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UntagCommand)) {
            return false;
        }

        UntagCommand otherUntagCommand = (UntagCommand) other;
        return employeeId.equals(otherUntagCommand.employeeId)
                && tagsToRemove.equals(otherUntagCommand.tagsToRemove);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("employeeId", employeeId)
                .add("tagsToRemove", tagsToRemove)
                .toString();
    }
}
