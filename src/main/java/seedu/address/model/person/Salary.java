package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.text.DecimalFormat;

/**
 * Represents a Person's salary in the address book, rounded to 2 decimal places.
 * @param value The salary in dollars.
 */
public record Salary(double value) implements Comparable<Salary> {
    public static final String MESSAGE_CONSTRAINTS = "Salary should be a positive number.";

    public Salary {
        checkArgument(value >= 0, MESSAGE_CONSTRAINTS);
    }

    @Override
    public double value() {
        return Double.parseDouble(String.format("%.2f", value));
    }

    /**
     * Converts the salary to cents.
     * @return The salary as cents.
     */
    public long inCents() {
        return Math.round(value * 100);
    }

    /**
     * Converts the salary to a double.
     * @return The salary as a double.
     */
    @Deprecated
    public double toDouble() {
        return value();
    }

    @Override
    public int compareTo(Salary other) {
        return Double.compare(this.value(), other.value());
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("#,###.##");
        return String.format("$%s / month", format.format(value()));
    }
}
