package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {
    private static long nextId = 0;

    // Identity fields
    private final String id = String.format("%04d", nextId++);
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Additional identity/data fields
    private final GitHubUsername gitHubUserName; // may be null if not provided
    private final Team team; // may be null if not provided

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.gitHubUserName = null;
        this.team = null;
        this.tags.addAll(tags);
    }

    /**
     * Full constructor including new fields.
     */
    public Person(
            Name name, Phone phone, Email email, Address address, Set<Tag> tags,
            GitHubUsername gitHubUserName, Team team
    ) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.gitHubUserName = gitHubUserName;
        this.team = team;
        this.tags.addAll(tags);
    }

    public String getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public GitHubUsername getGitHubUserName() {
        return gitHubUserName;
    }

    public Team getTeam() {
        return team;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person otherPerson)) {
            return false;
        }

        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .toString();
    }

    /**
     * Returns a builder pre-populated with this person's data, for convenient cloning with modifications.
     */
    public Builder duplicate() {
        return new Builder()
                .withName(name)
                .withPhone(phone)
                .withEmail(email)
                .withAddress(address)
                .withTags(tags)
                .withGitHubUserName(gitHubUserName)
                .withTeam(team);
    }

    /**
     * Builder for Person, allowing incremental construction.
     */
    public static class Builder {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags = new HashSet<>();
        private GitHubUsername gitHubUserName;
        private Team team;

        public Builder withName(Name name) {
            this.name = name;
            return this;
        }

        public Builder withPhone(Phone phone) {
            this.phone = phone;
            return this;
        }

        public Builder withEmail(Email email) {
            this.email = email;
            return this;
        }

        public Builder withAddress(Address address) {
            this.address = address;
            return this;
        }

        public Builder withTags(Tag... tags) {
            this.tags.addAll(Set.of(tags));
            return this;
        }

        public Builder withTags(Set<Tag> tags) {
            this.tags = new HashSet<>(tags);
            return this;
        }

        public Builder withGitHubUserName(GitHubUsername gitHubUserName) {
            this.gitHubUserName = gitHubUserName;
            return this;
        }

        public Builder withTeam(Team team) {
            this.team = team;
            return this;
        }

        public Person build() {
            // use full constructor; existing fields are required by Person invariant
            return new Person(name, phone, email, address, tags, gitHubUserName, team);
        }
    }

}
