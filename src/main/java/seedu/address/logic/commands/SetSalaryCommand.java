package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GITHUB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

public final class SetSalaryCommand extends Command {
    public static final String COMMAND_WORD = "set-salary";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets the salary for a person. "
            + "Parameters: <PERSON_ID> <SALARY>\n"
            + "Example: " + COMMAND_WORD + " "
            + "E12046 3670\n";

    public static final String MESSAGE_SUCCESS = "Set salary %1$d for: %2$s";
    public static final String MESSAGE_NON_EXISTENT_PERSON = "The person does not exist!";


    private final Person toSet;
    private final int salary;

    /**
     * Creates an SetSalaryCommand to add the specified {@code Person}
     */
    public SetSalaryCommand(Person person, int salary) {
        requireNonNull(person);
        toSet = person;
        this.salary = salary;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasPerson(toSet)) {
            throw new CommandException(MESSAGE_NON_EXISTENT_PERSON);
        }

        model.deletePerson(toSet);
        Person person = toSet.duplicate(toSet.id()).withSalary(salary).build();
        model.addPerson(person);
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
