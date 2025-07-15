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

package io.github.predatorray.timestone.test;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableTimeTest {
    @Test
    void testDefaultConstructorUsesSystemTime() {
        MutableTime mt = new MutableTime();
        long now = System.currentTimeMillis();
        long mtMillis = mt.instant().toEpochMilli();
        // Allow a small delta due to execution time
        assertTrue(Math.abs(now - mtMillis) < 1000);
    }

    @Test
    void testConstructorWithMillis() {
        long millis = 123456789L;
        MutableTime mt = new MutableTime(millis);
        assertEquals(millis, mt.instant().toEpochMilli());
    }

    @Test
    void testConstructorWithZone() {
        ZoneId zone = ZoneId.of("UTC");
        MutableTime mt = new MutableTime(zone);
        assertEquals(zone, mt.getZone());
    }

    @Test
    void testConstructorWithMillisAndZone() {
        long millis = 987654321L;
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        MutableTime mt = new MutableTime(millis, zone);
        assertEquals(millis, mt.instant().toEpochMilli());
        assertEquals(zone, mt.getZone());
    }

    @Test
    void testAdvance() {
        long millis = 1000L;
        MutableTime mt = new MutableTime(millis);
        mt.advance(Duration.ofMillis(500));
        assertEquals(millis + 500, mt.instant().toEpochMilli());
    }

    @Test
    void testAdvanceWithListener() {
        long millis = 1000L;
        MutableTime mt = new MutableTime(millis);
        long[] newTimeNotified = new long[1];
        mt.addListener(newTimeMillis -> newTimeNotified[0] = newTimeMillis);
        mt.advance(Duration.ofMillis(500));
        long expectedNewTime = millis + 500;
        assertEquals(expectedNewTime, mt.instant().toEpochMilli());
        assertEquals(expectedNewTime, newTimeNotified[0]);
    }

    @Test
    void testListenerIsNotNotifiedIfAddedAfterChanges() {
        long millis = 1000L;
        MutableTime mt = new MutableTime(millis);
        mt.advance(Duration.ofMillis(500));
        long[] newTimeNotified = new long[1];
        mt.addListener(newTimeMillis -> newTimeNotified[0] = newTimeMillis);
        assertEquals(millis + 500, mt.instant().toEpochMilli());
        assertEquals(0, newTimeNotified[0]);
    }

    @Test
    void testListenerIsNotNotifiedIfRemoved() {
        long millis = 1000L;
        MutableTime mt = new MutableTime(millis);
        long[] newTimeNotified = new long[1];
        MutableTimeListener mutableTimeListener = newTimeMillis -> newTimeNotified[0] = newTimeMillis;
        mt.addListener(mutableTimeListener);
        mt.removeListener(mutableTimeListener);

        mt.advance(Duration.ofMillis(500));
        assertEquals(millis + 500, mt.instant().toEpochMilli());
        assertEquals(0, newTimeNotified[0]);
    }

    @Test
    void testRemoveListenerDoesNothingIfNotAdded() {
        MutableTime mt = new MutableTime();
        MutableTimeListener listener = newTimeMillis -> {};
        mt.removeListener(listener); // Should not throw or do anything
    }

    @Test
    void testSleepAdvancesTime() throws InterruptedException {
        long millis = 2000L;
        MutableTime mt = new MutableTime(millis);
        mt.sleep(300);
        assertEquals(millis + 300, mt.instant().toEpochMilli());
    }

    @Test
    void testSleepInterrupts() {
        MutableTime mt = new MutableTime();
        Thread.currentThread().interrupt();
        assertThrows(InterruptedException.class, () -> mt.sleep(100));
        assertFalse(Thread.interrupted());
    }

    @Test
    void testWithZoneReturnsNewInstance() {
        MutableTime mt = new MutableTime(1000L, ZoneId.of("UTC"));
        MutableTime mt2 = mt.withZone(ZoneId.of("Asia/Tokyo"));
        assertEquals(ZoneId.of("Asia/Tokyo"), mt2.getZone());
        assertEquals(mt.instant(), mt2.instant());
    }
}
