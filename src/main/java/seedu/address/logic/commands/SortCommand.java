package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GITHUB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TEAM;

import java.util.Comparator;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sorts the list of persons by the specified field(s).
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts the list of persons by the specified field.\n"
            + "Parameters: "
            + "[" + PREFIX_NAME + "]"
            + "[" + PREFIX_ID + "]"
            + "[" + PREFIX_SALARY + "]"
            + "[" + PREFIX_PHONE + "]"
            + "[" + PREFIX_EMAIL + "]"
            + "[" + PREFIX_ADDRESS + "]"
            + "[" + PREFIX_GITHUB + "]"
            + "[" + PREFIX_TEAM + "]\n"
            + "Example: " + COMMAND_WORD + " -name";
    public static final String MESSAGE_SUCCESS = "Sorted the list of persons";

    private Comparator<Person> comparator;

    public SortCommand(Comparator<Person> comparator) {
        this.comparator = comparator;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        model.sortPersons(comparator);
        return new CommandResult(String.format(MESSAGE_SUCCESS));
    }
}
