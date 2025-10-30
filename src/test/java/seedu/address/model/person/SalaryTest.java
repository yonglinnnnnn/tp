package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.DecimalFormat;
import java.util.Random;

import org.junit.jupiter.api.Test;

class SalaryTest {
    private static final Random RAND = new Random();

    @Test
    void value() {
        double value = RAND.nextDouble(100000);
        assertEquals(new Salary(value).value(), Double.parseDouble(String.format("%.2f", value)));
    }

    @Test
    void inCents() {
        double value = RAND.nextDouble(100000);
        assertEquals(new Salary(value).inCents(), (int) Math.round(value * 100));
    }

    @Test
    void toStringMethod() {
        double value = RAND.nextDouble(100000);
        assertEquals(new Salary(value).toString(),
                     String.format("$%s / month", new DecimalFormat("#,###.##").format(value)));
    }

    @Test
    void value_withManyDecimalPlaces_roundedToTwoDecimalPlaces() {
        assertEquals(100.13, new Salary(100.13234).value());
        assertEquals(100.13, new Salary(100.12567).value());
    }

    @Test
    void compareTo() {
        double value1 = RAND.nextDouble(100000);
        double value2 = RAND.nextDouble(100000);
        assertEquals(Math.signum(value1 - value2), Math.signum(new Salary(value1).compareTo(new Salary(value2))));
    }
}
