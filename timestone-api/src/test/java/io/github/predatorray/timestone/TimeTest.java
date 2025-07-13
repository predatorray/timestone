package io.github.predatorray.timestone;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TimeTest {

    @Test
    void testSystemTime() {
        Time sytemTime = Time.SYSTEM;
        assertNotNull(sytemTime);
        assertInstanceOf(SystemTime.class, sytemTime);
    }

    @Test
    void testDefaultMillis() {
        long epochMillis = 1752354427775L;
        Instant instant = Instant.ofEpochMilli(epochMillis);
        Time implementation = new Time() {
            @Override
            public ZoneId getZone() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Time withZone(ZoneId zone) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Instant instant() {
                return instant;
            }

            @Override
            public void sleep(long millis) {
                throw new UnsupportedOperationException();
            }
        };
        assertEquals(epochMillis, implementation.millis());
    }
}
