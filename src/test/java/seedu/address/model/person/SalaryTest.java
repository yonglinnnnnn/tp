package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

class SalaryTest {
    Random rand = new Random();

    @Test
    void toDouble() {
        double value = rand.nextDouble(100000);
        assertEquals(new Salary(value).toDouble(), Double.parseDouble(String.format("%.2f", value)));
    }

    @Test
    void inCents() {
        double value = rand.nextDouble(100000);
        assertEquals(new Salary(value).inCents(), (int)Math.round(value * 100));
    }

    @Test
    void testToString() {
        double value = rand.nextDouble(100000);
        assertEquals(new Salary(value).toString(), String.format("$%.2f / month", value));
    }

    @Test
    void integerPart() {
        double value = rand.nextDouble(100000);
        assertEquals(new Salary(value).integerPart(), (int)value);
    }

    @Test
    void decimalPart() {
        double value = rand.nextDouble(100000);
        assertEquals(new Salary(value).decimalPart(), (int)Math.round((value - (int)value) * 100));
    }
}
