package seedu.address.model.audit;

import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages audit log entries for the address book.
 */
public class AuditLog {
    private final List<AuditLogEntry> entries;

    public AuditLog() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(String action, String details, LocalDateTime timestamp) {
        entries.add(new AuditLogEntry(timestamp, action, details));
    }

    public List<AuditLogEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public void clear() {
        entries.clear();
    }
}
