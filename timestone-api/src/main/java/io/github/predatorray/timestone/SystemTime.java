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

import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

/**
 * A {@link Time} implementation that uses the system clock.
 * <p>
 * Provides the current instant and allows sleeping for a specified duration.
 */
public class SystemTime extends ClockTime {

    private final ZoneId zoneId;

    /**
     * Constructs a {@code SystemTime} using the system default time zone.
     */
    public SystemTime() {
        this(ZoneId.systemDefault());
    }

    /**
     * Constructs a {@code SystemTime} with the specified time zone.
     *
     * @param zoneId the time zone to use, must not be null
     */
    public SystemTime(ZoneId zoneId) {
        this.zoneId = Objects.requireNonNull(zoneId, "zoneId must not be null");
    }

    /**
     * Gets the time zone associated with this clock.
     *
     * @return the time zone
     */
    @Override
    public ZoneId getZone() {
        return zoneId;
    }

    /**
     * Returns a copy of this {@code SystemTime} with a different time zone.
     *
     * @param zone the time zone to change to, not null
     * @return a {@code SystemTime} with the specified time zone
     */
    @Override
    public SystemTime withZone(ZoneId zone) {
        return new SystemTime(zone);
    }

    /**
     * Gets the current instant from the system clock.
     *
     * @return the current instant
     */
    @Override
    public Instant instant() {
        return Instant.now();
    }

    /**
     * Causes the current thread to sleep for the specified number of milliseconds.
     *
     * @param millis the length of time to sleep in milliseconds
     * @throws InterruptedException if any thread has interrupted the current thread
     */
    @Override
    public void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemTime that = (SystemTime) o;
        return Objects.equals(zoneId, that.zoneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zoneId);
    }
}
