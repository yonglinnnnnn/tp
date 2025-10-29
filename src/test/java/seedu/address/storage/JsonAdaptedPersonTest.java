package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.GitHubUsername;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Salary;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_GITHUBUSERNAME = "-johnd*oe";
    private static final String INVALID_SALARY = "fifty thousand";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_ID = BENSON.id();
    private static final String VALID_NAME = BENSON.name().fullName();
    private static final String VALID_PHONE = BENSON.phone().value();
    private static final String VALID_EMAIL = BENSON.email().value();
    private static final String VALID_ADDRESS = BENSON.address().value();
    private static final String VALID_GITHUBUSERNAME = BENSON.gitHubUsername().value();
    private static final String VALID_SALARY = String.valueOf(BENSON.salary().toDouble());
    private static final List<String> VALID_TEAMIDS = BENSON.teamIds().stream()
                                                        .toList();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.tags().stream()
                                                                 .map(JsonAdaptedTag::new)
                                                                 .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, INVALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_GITHUBUSERNAME, VALID_SALARY, VALID_TEAMIDS, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID, null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_GITHUBUSERNAME, VALID_SALARY,
                VALID_TEAMIDS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, VALID_NAME, INVALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_GITHUBUSERNAME, VALID_SALARY, VALID_TEAMIDS, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID, VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS, VALID_GITHUBUSERNAME, VALID_SALARY,
                VALID_TEAMIDS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, INVALID_EMAIL,
                        VALID_ADDRESS, VALID_GITHUBUSERNAME, VALID_SALARY, VALID_TEAMIDS, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID, VALID_NAME, VALID_PHONE, null, VALID_ADDRESS, VALID_GITHUBUSERNAME, VALID_SALARY,
                VALID_TEAMIDS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        INVALID_ADDRESS, VALID_GITHUBUSERNAME, VALID_SALARY, VALID_TEAMIDS, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL, null, VALID_GITHUBUSERNAME, VALID_SALARY,
                VALID_TEAMIDS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_GITHUBUSERNAME, VALID_SALARY, VALID_TEAMIDS, invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidGitHubUsername_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, INVALID_GITHUBUSERNAME, VALID_SALARY, VALID_TEAMIDS, VALID_TAGS);
        String expectedMessage = GitHubUsername.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullGitHubUsername_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, null, VALID_SALARY,
                VALID_TEAMIDS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, GitHubUsername.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullSalary_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_GITHUBUSERNAME, null, VALID_TEAMIDS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Salary.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidSalary_throwsNumberFormatException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_GITHUBUSERNAME, INVALID_SALARY, VALID_TEAMIDS, VALID_TAGS);
        assertThrows(NumberFormatException.class, person::toModelType);
    }

    @Test
    public void toModelType_nullTeamIds_createsPersonWithEmptyTeamIdSet() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_GITHUBUSERNAME, VALID_SALARY, null, VALID_TAGS);
        Person expectedPerson = new Person(BENSON.id(), BENSON.name(), BENSON.phone(), BENSON.email(),
                BENSON.address(), BENSON.gitHubUsername(), new HashSet<>(), BENSON.tags(), BENSON.salary());
        assertEquals(expectedPerson, person.toModelType());
    }

}
