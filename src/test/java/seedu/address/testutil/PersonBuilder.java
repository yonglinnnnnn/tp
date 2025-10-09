package seedu.address.testutil;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.parser.AddCommandParser;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.GitHubUsername;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {
    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_GITHUBUSERNAME = "amybee01";

    private String id;
    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private GitHubUsername gitHubUsername;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        Field field;
        try {
            field = AddCommandParser.class.getDeclaredField("nextId");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        field.setAccessible(true);
        try {
            id = String.format("E%04d", field.getLong(null));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        gitHubUsername = new GitHubUsername(DEFAULT_GITHUBUSERNAME);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        Field field;
        try {
            field = AddCommandParser.class.getDeclaredField("nextId");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        field.setAccessible(true);
        try {
            id = String.format("E%04d", field.getLong(null));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        name = personToCopy.name();
        phone = personToCopy.phone();
        email = personToCopy.email();
        address = personToCopy.address();
        gitHubUsername = personToCopy.gitHubUsername();
        tags = new HashSet<>(personToCopy.tags());
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy, boolean retainsId) {
        Field field;
        try {
            field = AddCommandParser.class.getDeclaredField("nextId");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        field.setAccessible(true);
        try {
            id = String.format("E%04d", field.getLong(null));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (retainsId) {
            id = personToCopy.id();
        }

        name = personToCopy.name();
        phone = personToCopy.phone();
        email = personToCopy.email();
        address = personToCopy.address();
        gitHubUsername = personToCopy.gitHubUsername();
        tags = new HashSet<>(personToCopy.tags());
    }

    /**
     * Sets the {@code Id} of the {@code Person} that we are building.
     * @param id The id to set.
     * @return The PersonBuilder object.
     */
    public PersonBuilder withId(long id) {
        this.id = String.format("E%04d", id);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code GitHubUsername} of the {@code Person} that we are building.
     */
    public PersonBuilder withGitHubUsername(String gitHubUsername) {
        this.gitHubUsername = new GitHubUsername(gitHubUsername);
        return this;
    }

    public Person build() {
        return new Person(id, name, phone, email, address, gitHubUsername, tags);
    }

}
