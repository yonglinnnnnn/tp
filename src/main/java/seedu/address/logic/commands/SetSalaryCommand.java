package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sets the salary for a person.
 */
public final class SetSalaryCommand extends Command {
    public static final String COMMAND_WORD = "set-salary";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets the salary for a person. "
            + "Parameters: <PERSON_ID> <SALARY>\n"
            + "Example: " + COMMAND_WORD + " "
            + "E12046 3670\n";

    public static final String MESSAGE_SUCCESS = "Set salary %1$d for: %2$s";
    public static final String MESSAGE_NON_EXISTENT_PERSON = "The person does not exist!";


    private final String toSet;
    private final int salary;

    /**
     * Creates an SetSalaryCommand for the specified {@code Person}
     */
    public SetSalaryCommand(String personId, int salary) {
        requireNonNull(personId);
        toSet = personId;
        this.salary = salary;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person person = model.find(p -> p.id().equals(toSet));
        model.deletePerson(person);
        model.addPerson(person.duplicate(toSet).withSalary(salary).build());
        model.addAuditEntry("SET SALARY", String.format("Set salary for person: %s", person.name()));
        return new CommandResult(String.format(MESSAGE_SUCCESS, salary, person.id()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SetSalaryCommand command)) {
            return false;
        }

        return command.toSet.equals(this.toSet) && command.salary == this.salary;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toSet", toSet)
                .add("salary", salary)
                .toString();
    }
}
