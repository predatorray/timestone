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

/**
 * Represents a source of time, providing access to the current instant and time zone.
 */
public interface Time {

    /**
     * The system time implementation using the default time zone.
     */
    Time SYSTEM = new SystemTime();

    /**
     * Returns the time zone associated with this time source.
     *
     * @return the {@link ZoneId} of this time source
     */
    ZoneId getZone();

    /**
     * Returns a copy of this time source with the specified time zone.
     *
     * @param zone the time zone to use
     * @return a {@code Time} instance with the given zone
     */
    Time withZone(ZoneId zone);

    /**
     * Returns the current time in milliseconds since the epoch.
     *
     * @return the current epoch milliseconds
     */
    default long millis() {
        return this.instant().toEpochMilli();
    }

    /**
     * Returns the current instant from this time source.
     *
     * @return the current {@link Instant}
     */
    Instant instant();

    /**
     * Suspends execution for the specified number of milliseconds.
     *
     * @param millis the length of time to sleep in milliseconds
     * @throws InterruptedException if the sleep is interrupted
     */
    void sleep(long millis) throws InterruptedException;
}
