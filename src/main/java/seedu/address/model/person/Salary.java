package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * Represents a Person's salary in the address book, rounded to 2 decimal places.
 * @param value The salary in dollars.
 */
public record Salary(double value) implements Comparable<Salary> {
    private static final String MESSAGE_CONSTRAINTS = "Salary should be a positive number in at most 2 decimal places";

    public Salary {
        checkArgument(value >= 0, MESSAGE_CONSTRAINTS);
    }

    /**
     * Converts the salary to cents.
     * @return The salary as cents.
     */
    public int inCents() {
        return (int)Math.round(value * 100);
    }

    @Override
    public int compareTo(Salary other) {
        return this.inCents() - other.inCents();
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("#,###.##");
        return String.format("$%s", format.format(inCents() / 100.0));
    }
}
