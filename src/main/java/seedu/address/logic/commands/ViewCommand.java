package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Filters and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Views all persons whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final NameContainsKeywordsPredicate predicate;

    public ViewCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);

        model.sortPersons(getComparator());

        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    private Comparator<Person> getComparator() {
        List<String> keywords = predicate.getKeywords();
        return Comparator
                // more matched keywords first
                .comparingInt((Person p) -> -computeMatchCount(p, keywords))
                // then lower total closeness score
                .thenComparingInt(p -> computeSumScore(p, keywords))
                // then by earliest matching keyword according to input order (lower = earlier keyword in input)
                .thenComparingInt(p -> computeFirstKeywordIndex(p, keywords))
                // finally by name (case-insensitive)
                .thenComparing(p -> p.name().fullName(), String::compareToIgnoreCase);
    }

    /**
     * Count how many keywords match the person's name (word or substring or exact).
     */
    private static int computeMatchCount(Person p, List<String> keywords) {
        String fullName = p.name().fullName();
        return (int) keywords.stream()
                .mapToInt(k -> scoreForKeyword(fullName, k))
                .filter(score -> score < 3) // 0,1,2 indicate a match
                .count();
    }

    /**
     * Sum of per-keyword scores (lower is better). Non-matching keywords contribute score 3.
     */
    private static int computeSumScore(Person p, List<String> keywords) {
        String fullName = p.name().fullName();
        return keywords.stream()
                .mapToInt(k -> scoreForKeyword(fullName, k))
                .sum();
    }

    /**
     * Compute a simple closeness score for a person given keywords.
     * Lower score = closer match for that keyword.
     * 0 = exact equals ignoring case
     * 1 = whole-word match (StringUtil.containsWordIgnoreCase)
     * 2 = substring match
     * 3 = no match
     */
    private static int scoreForKeyword(String fullName, String k) {
        if (fullName.equalsIgnoreCase(k)) {
            return 0;
        }
        if (StringUtil.containsWordIgnoreCase(fullName, k)) {
            return 1;
        }
        if (fullName.toLowerCase().contains(k.toLowerCase())) {
            return 2;
        }
        return 3;
    }

    /**
     * Returns the index of the first keyword (in input order) that matches the person's name.
     * Lower = the keyword appears earlier in the input. If no keyword matches, returns Integer.MAX_VALUE.
     */
    private static int computeFirstKeywordIndex(Person p, List<String> keywords) {
        String fullName = p.name().fullName();
        for (int i = 0; i < keywords.size(); i++) {
            if (scoreForKeyword(fullName, keywords.get(i)) < 3) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewCommand)) {
            return false;
        }

        ViewCommand otherViewCommand = (ViewCommand) other;
        return predicate.equals(otherViewCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
