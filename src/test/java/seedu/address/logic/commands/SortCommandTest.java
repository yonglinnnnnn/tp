package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.SortCommandParser;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.GitHubUsername;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

class SortCommandTest {
    private static final String[] VALID_TOKENS = { "name", "hp", "em", "addr", "gh", "id", "salary", "team" };

    private static final Random RAND = new Random();

    private Person makePerson(int i) {
        String id = String.format("%03d", i);
        String name = "Name" + i;
        String phone = String.valueOf(10000 + RAND.nextInt(90000));
        String email = "user" + i + "@example.com";
        String address = "Addr " + (char) ('A' + (i % 26)) + " #" + i;
        String gh = "@ghuser" + i;
        int salary = RAND.nextInt(5000) + i;
        List<String> teamIds = List.of("T" + (i % 3), "U" + (i % 2));

        return new Person.Builder(id)
                .withName(new Name(name))
                .withPhone(new Phone(phone))
                .withEmail(new Email(email))
                .withAddress(new Address(address))
                .withGitHubUserName(new GitHubUsername(gh))
                .withTeamIds(new HashSet<>(teamIds))
                .withSalary(salary)
                .build();
    }

    private List<Person> randomPeople(int n) {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            people.add(makePerson(i + 1));
        }
        Collections.shuffle(people, RAND);
        return people;
    }

    private boolean isSorted(List<Person> list, Comparator<Person> cmp) {
        for (int i = 1; i < list.size(); i++) {
            if (cmp.compare(list.get(i - 1), list.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    @Test
    void execute_differentComparators_becomeSorted() throws Exception {
        List<Person> base = randomPeople(15);

        // Try multiple random comparator chains
        for (int trial = 0; trial < 10; trial++) {
            // Create a fresh model with the same initial order
            Model model = new ModelManager();
            for (Person p : base) {
                model.addPerson(p);
            }

            // random non-empty subset of tokens
            List<String> tokens = new ArrayList<>(List.of(VALID_TOKENS));
            Collections.shuffle(tokens, RAND);
            int take = 1 + RAND.nextInt(tokens.size());
            String[] subset = tokens.subList(0, take).toArray(new String[0]);

            Comparator<Person> cmp = SortCommandParser.createComparator(subset);
            new SortCommand(cmp).execute(model);

            List<Person> result = new ArrayList<>(model.getFilteredPersonList());
            assertTrue(isSorted(result, cmp), "List should be sorted by " + String.join(",", subset));
        }
    }

    @Test
    void execute_defaultComparator_doesNotSort() throws Exception {
        // Build a model that is already sorted by name ascending
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // Ensure lexicographic ascending names
            String name = String.format("A%02d Person", i);
            Person p = new Person.Builder(String.format("%03d", i))
                    .withName(new Name(name))
                    .withPhone(new Phone("9000" + i))
                    .withEmail(new Email("a" + i + "@ex.com"))
                    .withAddress(new Address("Blk " + i))
                    .withGitHubUserName(new GitHubUsername("@user" + i))
                    .withTeamIds(new HashSet<>(List.of("T" + (i % 2))))
                    .withSalary(1000 + i)
                    .build();
            people.add(p);
        }

        Model model = new ModelManager();
        for (Person p : people) {
            model.addPerson(p);
        }

        List<Person> before = new ArrayList<>(model.getFilteredPersonList());

        // Default comparator is used when only "sort" is provided
        SortCommand defaultSort = new SortCommandParser().parse("sort");
        defaultSort.execute(model);

        List<Person> after = new ArrayList<>(model.getFilteredPersonList());
        assertEquals(before, after, "Order should remain unchanged when already sorted by default comparator (name).");
    }
}
