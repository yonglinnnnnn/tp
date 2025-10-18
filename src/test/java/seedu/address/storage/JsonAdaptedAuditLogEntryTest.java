package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;
import java.time.format.*;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.audit.AuditLogEntry;

public class JsonAdaptedAuditLogEntryTest {
    private static final String VALID_TIMESTAMP = "2024-01-15 10:30:45";
    private static final String VALID_ACTION = "ADD";
    private static final String VALID_DETAILS = "Added person: John Doe";
    private static final String INVALID_TIMESTAMP = "invalid-date";

    @Test
    public void toModelType_validAuditLogEntryDetails_returnsAuditLogEntry() throws Exception {
        AuditLogEntry auditLogEntry = new AuditLogEntry(
                LocalDateTime.parse(VALID_TIMESTAMP,
                        java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                VALID_ACTION,
                VALID_DETAILS);
        JsonAdaptedAuditLogEntry jsonAdaptedAuditLogEntry = new JsonAdaptedAuditLogEntry(auditLogEntry);
        assertEquals(auditLogEntry.getAction(), jsonAdaptedAuditLogEntry.toModelType().getAction());
        assertEquals(auditLogEntry.getDetails(), jsonAdaptedAuditLogEntry.toModelType().getDetails());
    }

    @Test
    public void toModelType_nullTimestamp_throwsIllegalValueException() {
        JsonAdaptedAuditLogEntry auditLogEntry = new JsonAdaptedAuditLogEntry(
                null, VALID_ACTION, VALID_DETAILS);
        String expectedMessage = "Missing audit log entry fields";
        assertThrows(IllegalValueException.class, expectedMessage, auditLogEntry::toModelType);
    }

    @Test
    public void toModelType_nullAction_throwsIllegalValueException() {
        JsonAdaptedAuditLogEntry auditLogEntry = new JsonAdaptedAuditLogEntry(
                VALID_TIMESTAMP, null, VALID_DETAILS);
        String expectedMessage = "Missing audit log entry fields";
        assertThrows(IllegalValueException.class, expectedMessage, auditLogEntry::toModelType);
    }

    @Test
    public void toModelType_nullDetails_throwsIllegalValueException() {
        JsonAdaptedAuditLogEntry auditLogEntry = new JsonAdaptedAuditLogEntry(
                VALID_TIMESTAMP, VALID_ACTION, null);
        String expectedMessage = "Missing audit log entry fields";
        assertThrows(IllegalValueException.class, expectedMessage, auditLogEntry::toModelType);
    }

    @Test
    public void toModelType_invalidTimestamp_throwsIllegalValueException() {
        JsonAdaptedAuditLogEntry auditLogEntry = new JsonAdaptedAuditLogEntry(
                INVALID_TIMESTAMP, VALID_ACTION, VALID_DETAILS);
        assertThrows(DateTimeParseException.class, auditLogEntry::toModelType);
    }
}
