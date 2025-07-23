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

package io.github.predatorray.timestone.examples;

import io.github.predatorray.timestone.test.MutableTime;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExponentialBackoffTest {

    @Test
    void testExponentialBackoff() throws InterruptedException {
        MutableTime time = new MutableTime();
        ExponentialBackoff backoff = new ExponentialBackoff(time, 100, 1600, 2.0);

        Instant startTime;
        long elapsedMillis;

        // Initial delay should be 100ms
        startTime = time.instant();
        backoff.backoff();
        elapsedMillis = time.instant().toEpochMilli() - startTime.toEpochMilli();
        assertEquals(100, elapsedMillis);

        // First backoff
        startTime = time.instant();
        backoff.backoff();
        elapsedMillis = time.instant().toEpochMilli() - startTime.toEpochMilli();
        assertEquals(200, elapsedMillis);

        // Second backoff
        startTime = time.instant();
        backoff.backoff();
        elapsedMillis = time.instant().toEpochMilli() - startTime.toEpochMilli();
        assertEquals(400, elapsedMillis);

        // Third backoff
        startTime = time.instant();
        backoff.backoff();
        elapsedMillis = time.instant().toEpochMilli() - startTime.toEpochMilli();
        assertEquals(800, elapsedMillis);

        // Fourth backoff should cap at max delay of 1600ms
        startTime = time.instant();
        backoff.backoff();
        elapsedMillis = time.instant().toEpochMilli() - startTime.toEpochMilli();
        assertEquals(1600, elapsedMillis);

        // Reset and backoff should start again from initial delay 100 ms
        backoff.reset();
        startTime = time.instant();
        backoff.backoff();
        elapsedMillis = time.instant().toEpochMilli() - startTime.toEpochMilli();
        assertEquals(100, elapsedMillis);
    }
}
