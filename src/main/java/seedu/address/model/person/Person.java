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
        Address address, GitHubUsername gitHubUsername,
        Set<String> teamIds, Set<Tag> tags, Salary salary
) {
    /**
     * Backwards-compatible constructor used in many places: creates a Person with no teams and default salary 0.
     */
    public Person(String id, Name name, Phone phone, Email email, Address address,
                  GitHubUsername gitHubUsername, Set<Tag> tags) {
        this(id, name, phone, email, address, gitHubGitNullSafe(gitHubUsername),
                new HashSet<>(), new HashSet<>(tags), new Salary(0));
    }

    /**
     * Canonical constructor.
     */
    public Person {
        requireAllNonNull(id, name, phone, email, address, gitHubGitNullSafe(gitHubUsername), tags, salary);
    }

    private static GitHubUsername gitHubGitNullSafe(GitHubUsername username) {
        // keep existing behavior: allow null GitHubUsername? adjust if you require non-null
        return username;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> tags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable view of the team id set.
     */
    public Set<String> teamIds() {
        return Collections.unmodifiableSet(teamIds);
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
                .withTeamIds(teamIds)
                .withSalaryInCents(salary.inCents());
    }

    /**
     * Returns a builder pre-populated with this person's data, for convenient cloning with modifications.
     */
    public Builder duplicate() {
        return new Builder(id)
                .withName(name)
                .withPhone(phone)
                .withEmail(email)
                .withAddress(address)
                .withTags(tags)
                .withGitHubUserName(gitHubUsername)
                .withTeamIds(teamIds)
                .withSalaryInCents(salary.inCents());
    }

    /**
     * Returns a new Person with the given team id added to the person's team id set.
     * Idempotent: adding an existing id has no effect.
     */
    public Person withAddedTeam(String newTeamId) {
        Set<String> newTeamIds = new HashSet<>(teamIds);
        newTeamIds.add(newTeamId);
        return new Person(id, name, phone, email, address, gitHubUsername, newTeamIds, new HashSet<>(tags), salary);
    }

    /**
     * Returns a new Person with the given team id removed from the person's team id set.
     * Idempotent: removing a non-existing id has no effect.
     */
    public Person withRemovedTeam(String removeTeamId) {
        Set<String> newTeamIds = new HashSet<>(teamIds);
        newTeamIds.remove(removeTeamId);
        return new Person(id, name, phone, email, address, gitHubUsername, newTeamIds, new HashSet<>(tags), salary);
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
        private Set<String> teamIds = new HashSet<>();
        private Salary salary;

        /**
         * Constructs a {@code Builder} with the given {@code id}.
         * @param id The id of the person.
         */
        public Builder(String id) {
            this.id = id;
        }

        /**
         * Sets the {@code Name} of the {@code Person} that we are building.
         * @param name The name to set.
         * @return This builder object.
         */
        public Builder withName(Name name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the {@code Phone} of the {@code Person} that we are building.
         * @param phone The phone to set.
         * @return This builder object.
         */
        public Builder withPhone(Phone phone) {
            this.phone = phone;
            return this;
        }

        /**
         * Sets the {@code Email} of the {@code Person} that we are building.
         * @param email The email to set.
         * @return This builder object.
         */
        public Builder withEmail(Email email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the {@code Address} of the {@code Person} that we are building.
         * @param address The address to set.
         * @return This builder object.
         */
        public Builder withAddress(Address address) {
            this.address = address;
            return this;
        }

        /**
         * Adds the given {@code Tag}s to the {@code Person} that we are building.
         * @param tags The tags to add.
         * @return This builder object.
         */
        public Builder withTags(Tag... tags) {
            this.tags.addAll(Set.of(tags));
            return this;
        }

        /**
         * Sets the {@code tags} of the {@code Person} that we are building.
         * @param tags The set of tags to set.
         * @return This builder object.
         */
        public Builder withTags(Set<Tag> tags) {
            this.tags = new HashSet<>(tags);
            return this;
        }

        /**
         * Sets the {@code GitHubUsername} of the {@code Person} that we are building.
         * @param gitHubUserName The GitHub username to set.
         * @return This builder object.
         */
        public Builder withGitHubUserName(GitHubUsername gitHubUserName) {
            this.gitHubUsername = gitHubUserName;
            return this;
        }

        /**
         * Sets the {@code teamIds} of the {@code Person} that we are building.
         * @param teamIds The set of team IDs to set.
         * @return This builder object.
         */
        public Builder withTeamIds(Set<String> teamIds) {
            this.teamIds = new HashSet<>(teamIds);
            return this;
        }

        /**
         * Sets the {@code salary} of the {@code Person} that we are building.
         * @param salaryInDollars The salary to set.
         * @return This builder object.
         */
        public Builder withSalary(double salaryInDollars) {
            this.salary = new Salary(salaryInDollars);
            return this;
        }

        /**
         * Sets the {@code salary} of the {@code Person} that we are building. Use this if you require exact precision.
         * @param salaryInCents The salary to set.
         * @return This builder object.
         */
        public Builder withSalaryInCents(int salaryInCents) {
            this.salary = new Salary(salaryInCents / 100.0);
            return this;
        }

        /**
         * Builds and returns the {@code Person} object.
         * @return The constructed Person object.
         */
        public Person build() {
            return new Person(id, name, phone, email, address, gitHubUsername,
                    new HashSet<>(teamIds), new HashSet<>(tags), salary);
        }
    }
}
