package seedu.address.model.audit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an audit log entry in the address book.
 * Each entry contains a timestamp, an action performed, and details about that action.
 * Audit log entries are immutable once created.
 */
public class AuditLogEntry {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime timestamp;
    private final String action;
    private final String details;

    /**
     * Constructs an AuditLogEntry with the specified timestamp, action, and details.
     *
     * @param timestamp The date and time when the action occurred.
     * @param action The type of action performed (e.g., "ADD", "DELETE", "EDIT").
     * @param details Additional information about the action.
     */
    public AuditLogEntry(LocalDateTime timestamp, String action, String details) {
        this.timestamp = timestamp;
        this.action = action;
        this.details = details;
    }

    /**
     * Returns the timestamp of when the action occurred.
     *
     * @return The timestamp of this audit log entry.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the action that was performed.
     *
     * @return The action of this audit log entry.
     */
    public String getAction() {
        return action;
    }

    /**
     * Returns the details of the action.
     *
     * @return The details of this audit log entry.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Returns a formatted string representation of this audit log entry.
     * The format is: "[yyyy-MM-dd HH:mm:ss] action: details"
     *
     * @return A formatted string representation of this audit log entry.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s: %s",
                timestamp.format(FORMATTER), action, details);
    }
}
