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
 * Adds tags to an existing person in the address book.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds tags to the person identified "
            + "by their employee ID.\n"
            + "Parameters: EMPLOYEE_ID (must start with E) TAG [MORE_TAGS]...\n"
            + "Example: " + COMMAND_WORD + " E1001 friends colleagues";

    public static final String MESSAGE_TAG_SUCCESS = "Added tags to Person: %1$s";
    public static final String MESSAGE_NO_TAGS_PROVIDED = "At least one tag must be provided.";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No person with employee ID %1$s found.";

    private final String employeeId;
    private final Set<Tag> tagsToAdd;

    /**
     * @param employeeId of the person to add tags to
     * @param tagsToAdd tags to add to the person
     */
    public TagCommand(String employeeId, Set<Tag> tagsToAdd) {
        requireNonNull(employeeId);
        requireNonNull(tagsToAdd);

        this.employeeId = employeeId;
        this.tagsToAdd = tagsToAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Person personToTag = lastShownList.stream()
                .filter(person -> person.id().equals(employeeId))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, employeeId)));

        Person taggedPerson = createTaggedPerson(personToTag, tagsToAdd);

        model.setPerson(personToTag, taggedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.addAuditEntry("TAG", String.format("Add tag for person: %s", taggedPerson.name()));

        return new CommandResult(String.format(MESSAGE_TAG_SUCCESS, Messages.format(taggedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the tags of {@code personToTag}
     * combined with {@code tagsToAdd}.
     */
    private static Person createTaggedPerson(Person personToTag, Set<Tag> tagsToAdd) {
        assert personToTag != null;

        Set<Tag> updatedTags = new HashSet<>(personToTag.tags());
        updatedTags.addAll(tagsToAdd);

        return personToTag.duplicate(personToTag.id())
                .withTags(updatedTags)
                .build();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherTagCommand = (TagCommand) other;
        return employeeId.equals(otherTagCommand.employeeId)
                && tagsToAdd.equals(otherTagCommand.tagsToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("employeeId", employeeId)
                .add("tagsToAdd", tagsToAdd)
                .toString();
    }
}
