package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.model.Model;
import seedu.address.model.audit.AuditLog;
import seedu.address.model.audit.AuditLogEntry;

/**
 * Displays the audit log of all past actions.
 */
public class AuditCommand extends Command {

    public static final String COMMAND_WORD = "audit";
    public static final String MESSAGE_SUCCESS = "Audit Log:\n%s";
    public static final String MESSAGE_EMPTY = "No actions recorded in audit log.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        AuditLog auditLog = model.getAuditLog();
        List<AuditLogEntry> entries = auditLog.getEntries();

        if (entries.isEmpty()) {
            return new CommandResult("No audit log entries found.");
        }

        StringBuilder result = new StringBuilder("Audit Log:\n");
        for (AuditLogEntry entry : entries) {
            result.append(entry.toString()).append("\n");
        }

        return new CommandResult(result.toString());
    }
}
