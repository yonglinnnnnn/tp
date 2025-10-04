package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public record Person(
        String id, Name name, Phone phone, Email email,
        Address address, GitHubUsername gitHubUsername, Team team, Set<Tag> tags
) {
    /**
     * Every field must be present and not null.
     */
    public Person(String id, Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(id, name, phone, email, address, null, null, new HashSet<>());
        this.tags.addAll(tags);
    }

    public Person {
        requireAllNonNull(id, name, phone, email, address, tags);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> tags() {
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

        return otherPerson != null && otherPerson.name.equals(name);
    }

    /**
     * Returns a builder pre-populated with this person's data, for convenient cloning with modifications.
     * @param newId The new ID to use.
     */
    public Builder duplicate(String newId) {
        requireAllNonNull(newId);
        return new Builder(newId)
                .withName(name)
                .withPhone(phone)
                .withEmail(email)
                .withAddress(address)
                .withTags(tags)
                .withGitHubUserName(gitHubUsername)
                .withTeam(team);
    }

    /**
     * Builder for Person, allowing incremental construction.
     */
    public static class Builder {
        private String id;
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags = new HashSet<>();
        private GitHubUsername gitHubUsername;
        private Team team;

        private Builder() { }

        public Builder(String id) {
            this.id = id;
        }

        /**
         * Sets the {@code Name} of the {@code Person} that we are building.
         * @param name The name to set.
         * @return The Builder object.
         */
        public Builder withName(Name name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the {@code Phone} of the {@code Person} that we are building.
         * @param phone The phone to set.
         * @return The Builder object.
         */
        public Builder withPhone(Phone phone) {
            this.phone = phone;
            return this;
        }

        /**
         * Sets the {@code Email} of the {@code Person} that we are building.
         * @param email The email to set.
         * @return The Builder object.
         */
        public Builder withEmail(Email email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the {@code Address} of the {@code Person} that we are building.
         * @param address The address to set.
         * @return The Builder object.
         */
        public Builder withAddress(Address address) {
            this.address = address;
            return this;
        }

        /**
         * Sets the {@code Tags} of the {@code Person} that we are building.
         * @param tags The tags to set.
         * @return The Builder object.
         */
        public Builder withTags(Tag... tags) {
            this.tags.addAll(Set.of(tags));
            return this;
        }

        /**
         * Sets the {@code Tags} of the {@code Person} that we are building.
         * @param tags The tags to set.
         * @return The Builder object.
         */
        public Builder withTags(Set<Tag> tags) {
            this.tags = new HashSet<>(tags);
            return this;
        }

        /**
         * Sets the GitHub username of the {@code Person} that we are building.
         * @param gitHubUserName The GitHub username to set.
         * @return The Builder object.
         */
        public Builder withGitHubUserName(GitHubUsername gitHubUserName) {
            this.gitHubUsername = gitHubUserName;
            return this;
        }

        /**
         * Sets the team of the {@code Person} that we are building.
         * @param team The team to set.
         * @return The Builder object.
         */
        public Builder withTeam(Team team) {
            this.team = team;
            return this;
        }

        /**
         * Builds the Person object.
         * @return The Person object.
         */
        public Person build() {
            return new Person(id, name, phone, email, address, gitHubUsername, team, tags);
        }
    }

}
