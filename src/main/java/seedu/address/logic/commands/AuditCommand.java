package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Displays the audit log of all past actions.
 */
public class AuditCommand extends Command {

    public static final String COMMAND_WORD = "audit";
    public static final String MESSAGE_SUCCESS = "Audit Log:\n%s";
    public static final String MESSAGE_EMPTY = "No actions recorded in audit log.";

    @Override
    public CommandResult execute(Model model) {
        var entries = model.getAuditLog().getEntries();

        if (entries.isEmpty()) {
            return new CommandResult(MESSAGE_EMPTY);
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < entries.size(); i++) {
            result.append(String.format("%d. %s\n", i + 1, entries.get(i)));
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, result.toString().trim()));
    }
}