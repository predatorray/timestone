/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2025 Wenhao Ji <predator.ray@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.predatorray.timestone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneId;
import java.time.Instant;

import org.junit.jupiter.api.Test;

class SystemTimeTest {

    @Test
    void testDefaultZone() {
        SystemTime systemTime = new SystemTime();
        assertEquals(ZoneId.systemDefault(), systemTime.getZone());
    }

    @Test
    void testCustomZone() {
        ZoneId zone = ZoneId.of("UTC");
        SystemTime systemTime = new SystemTime(zone);
        assertEquals(zone, systemTime.getZone());
    }

    @Test
    void testWithZone() {
        SystemTime systemTime = new SystemTime();
        ZoneId zone = ZoneId.of("Asia/Tokyo");
        SystemTime newSystemTime = systemTime.withZone(zone);
        assertEquals(zone, newSystemTime.getZone());
        assertNotEquals(systemTime.getZone(), newSystemTime.getZone());
    }

    @Test
    void testInstant() {
        SystemTime systemTime = new SystemTime();
        Instant before = Instant.now();
        Instant actual = systemTime.instant();
        Instant after = Instant.now();
        assertTrue(!actual.isBefore(before) && !actual.isAfter(after));
    }

    @Test
    void testSleep() throws InterruptedException {
        SystemTime systemTime = new SystemTime();
        long start = System.currentTimeMillis();
        systemTime.sleep(100);
        long end = System.currentTimeMillis();
        assertTrue(end - start >= 100);
    }

    @Test
    void testEqualsAndHashCode_sameZone() {
        ZoneId zone = ZoneId.of("UTC");
        SystemTime time1 = new SystemTime(zone);
        SystemTime time2 = new SystemTime(zone);
        assertEquals(time1, time2);
        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_differentZone() {
        SystemTime time1 = new SystemTime(ZoneId.of("UTC"));
        SystemTime time2 = new SystemTime(ZoneId.of("Asia/Tokyo"));
        assertNotEquals(time1, time2);
        assertNotEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    void testEquals_nullAndOtherType() {
        SystemTime time = new SystemTime(ZoneId.of("UTC"));
        assertNotEquals(time, null);
        assertNotEquals(time, "not a SystemTime");
    }
}
