package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_TAG_UNALLOWED;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.GITHUBUSERNAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GITHUBUSERNAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GITHUBUSERNAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.GitHubUsername;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;
    private static final String VALID_EMPLOYEE_ID_1 = "E0001";
    private static final String VALID_EMPLOYEE_ID_2 = "E0002";
    private static final String VALID_EMPLOYEE_ID_3 = "E0003";

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no employee ID specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, VALID_EMPLOYEE_ID_1, EditCommand.MESSAGE_NOT_EDITED);

        // no employee ID and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // invalid employee ID format - lowercase
        assertParseFailure(parser, "e1234" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid employee ID format - too short
        assertParseFailure(parser, "E123" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid employee ID format - too long
        assertParseFailure(parser, "E12345" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid employee ID format - no E prefix
        assertParseFailure(parser, "1234" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "E1234 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "E1234 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, VALID_EMPLOYEE_ID_1 + INVALID_NAME_DESC,
                Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, VALID_EMPLOYEE_ID_1 + INVALID_PHONE_DESC,
                Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, VALID_EMPLOYEE_ID_1 + INVALID_EMAIL_DESC,
                Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, VALID_EMPLOYEE_ID_1 + INVALID_ADDRESS_DESC,
                Address.MESSAGE_CONSTRAINTS); // invalid address
        assertParseFailure(parser, VALID_EMPLOYEE_ID_1 + INVALID_GITHUBUSERNAME_DESC,
                GitHubUsername.MESSAGE_CONSTRAINTS); // invalid GitHub username

        // invalid phone followed by valid email
        assertParseFailure(parser, VALID_EMPLOYEE_ID_1 + INVALID_PHONE_DESC + EMAIL_DESC_AMY,
                Phone.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, VALID_EMPLOYEE_ID_1 + INVALID_NAME_DESC + INVALID_EMAIL_DESC
                + VALID_ADDRESS_AMY + VALID_PHONE_AMY, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        String userInput = VALID_EMPLOYEE_ID_2 + PHONE_DESC_BOB
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .build();
        EditCommand expectedCommand = new EditCommand(VALID_EMPLOYEE_ID_2, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        String userInput = VALID_EMPLOYEE_ID_1 + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand = new EditCommand(VALID_EMPLOYEE_ID_1, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        String userInput = VALID_EMPLOYEE_ID_3 + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(VALID_EMPLOYEE_ID_3, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = VALID_EMPLOYEE_ID_3 + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(VALID_EMPLOYEE_ID_3, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = VALID_EMPLOYEE_ID_3 + EMAIL_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCommand(VALID_EMPLOYEE_ID_3, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = VALID_EMPLOYEE_ID_3 + ADDRESS_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditCommand(VALID_EMPLOYEE_ID_3, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // GitHub username
        userInput = VALID_EMPLOYEE_ID_3 + GITHUBUSERNAME_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withGitHubUsername(VALID_GITHUBUSERNAME_AMY).build();
        expectedCommand = new EditCommand(VALID_EMPLOYEE_ID_3, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_tagFieldSpecified_failure() {
        String userInput = VALID_EMPLOYEE_ID_1 + TAG_DESC_FRIEND;

        assertParseFailure(parser, userInput, MESSAGE_TAG_UNALLOWED);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        String userInput = VALID_EMPLOYEE_ID_1 + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = VALID_EMPLOYEE_ID_1 + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // mulltiple valid fields repeated
        userInput = VALID_EMPLOYEE_ID_1 + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + PHONE_DESC_BOB + ADDRESS_DESC_BOB + EMAIL_DESC_BOB;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));

        // multiple invalid values
        userInput = VALID_EMPLOYEE_ID_1 + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));
    }
}
