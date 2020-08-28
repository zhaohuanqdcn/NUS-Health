package com.orbidroid.orbidroid_backend.helper.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdderTest {

    @Test
    void add() {
        // pure adding minutes
        assertEquals("2020-02-02 12:20:00", Adder.add("2020-02-02 12:00:00", 20));
        assertEquals("2020-02-02 17:30:00", Adder.add("2020-02-02 12:00:00", 330));

        // adding minutes results in hour change
        assertEquals("2020-03-20 12:40:00", Adder.add("2020-03-20 11:50:00", 50));

        // adding minutes results in day change
        assertEquals("2020-03-21 00:30:00", Adder.add("2020-03-20 23:40:00", 50));

        // adding minutes results in month change
        assertEquals("2020-04-01 00:30:00", Adder.add("2020-03-31 23:40:00", 50));

        // adding minutes results in year change
        assertEquals("2021-01-01 00:30:00", Adder.add("2020-12-31 23:40:00", 50));

        // case where format check cannot pass
        assertEquals("2021-01-01 00:30:e0", Adder.add("2021-01-01 00:30:e0", 50));
    }
}