package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        int personCount = model.getAddressBook().getPersonList().size();
        model.setAddressBook(new AddressBook());

        model.addAuditEntry("CLEAR", String.format("Cleared all data (%d persons)", personCount));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
