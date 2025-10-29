package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
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

    public static final String MESSAGE_UNTAG_SUCCESS = "Removed tags from: %s";
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

        // Find the person with the matching employee ID
        Person personToUntag = lastShownList.stream()
                .filter(person -> person.id().equals(employeeId))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, employeeId)));

        // Validate that all tags to remove exist on the person (case-insensitive)
        Set<Tag> personTags = personToUntag.tags();
        Set<Tag> nonExistentTags = new HashSet<>();
        Set<Tag> actualTagsToRemove = new HashSet<>();

        for (Tag tagToRemove : tagsToRemove) {
            boolean found = false;
            for (Tag personTag : personTags) {
                if (personTag.tagName.equalsIgnoreCase(tagToRemove.tagName)) {
                    actualTagsToRemove.add(personTag);
                    found = true;
                    break;
                }
            }
            if (!found) {
                nonExistentTags.add(tagToRemove);
            }
        }

        // Build result message with removed tags
        String resultMessage;
        if (actualTagsToRemove.isEmpty()) {
            resultMessage = String.format("No tags were removed from: %s", employeeId);
        } else {
            String removedTags = formatTagNames(actualTagsToRemove);
            resultMessage = String.format("Removed tags from %s: %s", employeeId, removedTags);
        }

        if (!nonExistentTags.isEmpty()) {
            String invalidTags = formatTagNames(nonExistentTags);
            resultMessage += "\n" + String.format(MESSAGE_TAG_NOT_FOUND, invalidTags);
        }

        return new CommandResult(resultMessage);
    }

    private String formatTagNames(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> tag.tagName)
                .sorted()
                .collect(Collectors.joining(", "));
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
