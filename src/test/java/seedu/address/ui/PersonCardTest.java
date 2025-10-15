package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;


public class PersonCardTest {

    @BeforeAll
    public static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // already started - nothing to do
        }
    }

    @Test
    public void createPersonCard_validPerson_success() {
        Person samplePerson = TypicalPersons.ALICE;

        int displayedIndex = 1;
        PersonCard card = new PersonCard(samplePerson, displayedIndex);
        assertNotNull(card);
    }
}
