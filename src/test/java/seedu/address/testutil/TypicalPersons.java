package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GITHUBUSERNAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GITHUBUSERNAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder()
            .withId(0)
            .withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withEmail("alice@example.com")
            .withPhone("94351253")
            .withGitHubUsername("@alice01")
            .withTags("friends").build();
    public static final Person BENSON = new PersonBuilder()
            .withId(1)
            .withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com")
            .withPhone("98765432")
            .withGitHubUsername("@benson02")
            .withTags("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder()
            .withId(2)
            .withName("Carl Kurz")
            .withPhone("95352563")
            .withEmail("heinz@example.com")
            .withGitHubUsername("@carl03")
            .withAddress("wall street")
            .withoutTags().build();
    public static final Person DANIEL = new PersonBuilder()
            .withId(3)
            .withName("Daniel Meier")
            .withPhone("87652533")
            .withEmail("cornelia@example.com")
            .withAddress("10th street")
            .withGitHubUsername("@daniel04")
            .withTags("friends").build();
    public static final Person ELLE = new PersonBuilder()
            .withId(4)
            .withName("Elle Meyer")
            .withPhone("9482224")
            .withEmail("werner@example.com")
            .withGitHubUsername("@elle05")
            .withAddress("michegan ave")
            .withoutTags().build();
    public static final Person FIONA = new PersonBuilder()
            .withId(5)
            .withName("Fiona Kunz")
            .withPhone("9482427")
            .withEmail("lydia@example.com")
            .withGitHubUsername("@fiona06")
            .withAddress("little tokyo")
            .withoutTags().build();
    public static final Person GEORGE = new PersonBuilder()
            .withId(6)
            .withName("George Best")
            .withPhone("9482442")
            .withEmail("anna@example.com")
            .withGitHubUsername("@george07")
            .withAddress("4th street")
            .withoutTags().build();

    // Manually added
    public static final Person HOON = new PersonBuilder()
            .withId(7)
            .withName("Hoon Meier")
            .withPhone("8482424")
            .withEmail("stefan@example.com")
            .withGitHubUsername("@hoon08")
            .withAddress("little india")
            .withoutTags().build();
    public static final Person IDA = new PersonBuilder()
            .withId(8)
            .withName("Ida")
            .withPhone("8482131")
            .withEmail("hans@example.com")
            .withGitHubUsername("@ida09")
            .withAddress("chicago ave")
            .withoutTags().build();
    public static final Person IDAsecond = new PersonBuilder()
            .withId(9)
            .withName("Idaline Whatever")
            .withPhone("2132321")
            .withEmail("hans@example.com")
            .withGitHubUsername("@ida09")
            .withAddress("chicago ave")
            .withoutTags().build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder()
            .withId(9)
            .withName(VALID_NAME_AMY)
            .withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY)
            .withAddress(VALID_ADDRESS_AMY)
            .withGitHubUsername(VALID_GITHUBUSERNAME_AMY)
            .build();
    public static final Person BOB = new PersonBuilder()
            .withId(10)
            .withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB)
            .withAddress(VALID_ADDRESS_BOB)
            .withGitHubUsername(VALID_GITHUBUSERNAME_BOB)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
