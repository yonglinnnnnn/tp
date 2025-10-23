package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.GitHubUsername;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

class SortCommandParserTest {
    private final static Random RAND = new Random();

    private final static String[] VALID_TOKENS = {
            "name",
            "hp",
            "em",
            "addr",
            "gh",
            "id",
            "salary",
            "team"
    };

    private final static String[] EXAMPLE_INVALID_TOKENS = {
            "phone",
            "email",
            "address",
            "github"
    };

    // Produce "sort" or "sort -name -hp ..." correctly (space before first "-")
    private String getRandomValidCommand() {
        List<String> tokens = new ArrayList<>(Arrays.asList(VALID_TOKENS));
        Collections.shuffle(tokens, RAND);
        int numTaken = tokens.isEmpty() ? 0 : RAND.nextInt(tokens.size() + 1); // 0..size
        if (numTaken == 0) {
            return "sort";
        }
        List<String> taken = tokens.subList(0, numTaken);
        return "sort -" + String.join(" -", taken);
    }

    // Mix at least one invalid token among possibly some valid ones; ensure proper spacing
    private String getRandomInvalidCommand() {
        List<String> valids = new ArrayList<>(Arrays.asList(VALID_TOKENS));
        List<String> invalids = new ArrayList<>(Arrays.asList(EXAMPLE_INVALID_TOKENS));
        Collections.shuffle(valids, RAND);
        Collections.shuffle(invalids, RAND);

        int numValid = RAND.nextInt(valids.size() + 1); // 0..size
        int numInvalid = 1 + RAND.nextInt(Math.max(1, invalids.size())); // at least 1 invalid
        List<String> picked = new ArrayList<>();
        picked.addAll(valids.subList(0, numValid));
        picked.addAll(invalids.subList(0, numInvalid));
        Collections.shuffle(picked, RAND);

        return "sort -" + String.join(" -", picked);
    }

    private Person buildPerson(
            String id, String name, String phone, String email, String address,
            String gh, List<String> teamIds, int salary
    ) {
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

    @Test
    void createComparator_validTokens_success() throws Exception {
        // Prepare two persons where p1 should be "less than" p2 by all fields
        Person p1 = buildPerson(
                "001", "Alice A", "1111", "a@example.com", "Alpha Street",
                "@aaa", List.of("A", "B"), 1000
        );
        Person p2 = buildPerson(
                "002", "Bob B", "9999", "z@example.com", "Zulu Street",
                "@zzz", List.of("Z"), 2000
        );

        for (int i = 0; i < 30; i++) {
            // random non-empty subset of valid tokens
            List<String> tokens = new ArrayList<>(Arrays.asList(VALID_TOKENS));
            Collections.shuffle(tokens, RAND);
            int take = 1 + RAND.nextInt(tokens.size()); // at least one
            String[] subset = tokens.subList(0, take).toArray(new String[0]);

            var comparator = SortCommandParser.createComparator(subset);
            assertTrue(comparator.compare(p1, p2) < 0, "Expected p1 < p2 for tokens " + Arrays.toString(subset));
            assertTrue(comparator.compare(p2, p1) > 0, "Expected p2 > p1 for tokens " + Arrays.toString(subset));
            assertEquals(0, comparator.compare(p1, p1));
            assertEquals(0, comparator.compare(p2, p2));
        }
    }

    @Test
    void createComparator_containsInvalidTokens_throwsParseException() {
        for (int i = 0; i < 20; i++) {
            String[] tokensWithInvalid = getRandomInvalidCommand()
                    .replaceFirst("^sort -", "") // strip leading "sort -"
                    .split(" -");
            assertThrows(ParseException.class, () -> SortCommandParser.createComparator(tokensWithInvalid));
        }
    }

    @Test
    void parse_validCommand_success() throws Exception {
        // p1 should appear before p2 for any valid comparator chain and also for default name sort
        Person p1 = buildPerson(
                "001", "Alice A", "1111", "a@example.com", "Alpha Street",
                "@aaa", List.of("A", "B"), 1000
        );
        Person p2 = buildPerson(
                "002", "Bob B", "9999", "z@example.com", "Zulu Street",
                "@zzz", List.of("Z"), 2000
        );

        for (int i = 0; i < 50; i++) {
            String cmdText = getRandomValidCommand();
            SortCommand cmd = new SortCommandParser().parse(cmdText);
            assertNotNull(cmd);

            Model model = new ModelManager();
            // add in reverse order to validate sorting effect
            model.addPerson(p2);
            model.addPerson(p1);

            cmd.execute(model);
            var list = model.getFilteredPersonList();
            assertEquals(2, list.size());
            assertEquals(p1, list.get(0));
            assertEquals(p2, list.get(1));
        }
    }

    @Test
    void parse_invalidCommand_throwsParseException() {
        for (int i = 0; i < 20; i++) {
            String cmdText = getRandomInvalidCommand();
            assertThrows(ParseException.class, () -> new SortCommandParser().parse(cmdText));
        }
    }
}
