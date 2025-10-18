package seedu.address.storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.audit.AuditLogEntry;

/**
 * Jackson-friendly version of {@link AuditLogEntry}.
 */
class JsonAdaptedAuditLogEntry {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String timestamp;
    private final String action;
    private final String details;

    @JsonCreator
    public JsonAdaptedAuditLogEntry(@JsonProperty("timestamp") String timestamp,
                                    @JsonProperty("action") String action,
                                    @JsonProperty("details") String details) {
        this.timestamp = timestamp;
        this.action = action;
        this.details = details;
    }

    public JsonAdaptedAuditLogEntry(AuditLogEntry source) {
        timestamp = source.getTimestamp().format(FORMATTER);
        action = source.getAction();
        details = source.getDetails();
    }

    public AuditLogEntry toModelType() throws IllegalValueException {
        if (timestamp == null || action == null || details == null) {
            throw new IllegalValueException("Missing audit log entry fields");
        }
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, FORMATTER);
        return new AuditLogEntry(dateTime, action, details);
    }
}