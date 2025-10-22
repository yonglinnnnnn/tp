package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's salary in the address book, rounded to 2 decimal places.
 * @param integerPart The integer part of the salary.
 * @param decimalPart The decimal part of the salary.
 */
public record Salary(int integerPart, int decimalPart) {
    private static final String MESSAGE_CONSTRAINTS = "Salary should be a positive number in at most 2 decimal places";

    public Salary {
        checkArgument(integerPart >= 0 && decimalPart >= 0 && decimalPart < 100, MESSAGE_CONSTRAINTS);
    }

    /**
     * Creates a {@code Salary} from a decimal value, rounded to 2 decimal places.
     * @param value The decimal value of the salary.
     */
    public Salary(double value) {
        this((int)Math.floor(value), (int)Math.round((value - Math.floor(value)) * 100));
    }

    @Override
    public String toString() {
        return String.format("$%.2f / month", integerPart + decimalPart / 100.0);
    }
}
