package seedu.address.model.audit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an audit log entry in the address book.
 */
public class AuditLogEntry {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime timestamp;
    private final String action;
    private final String details;

    public AuditLogEntry(LocalDateTime timestamp, String action, String details) {
        this.timestamp = timestamp;
        this.action = action;
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s",
                timestamp.format(FORMATTER), action, details);
    }
}
