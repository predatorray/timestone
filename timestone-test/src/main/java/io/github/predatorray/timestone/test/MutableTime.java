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

import io.github.predatorray.timestone.ClockTime;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for unit tests that allows manipulation of the current time.
 *
 * <p>
 * This class is designed to facilitate testing of time-dependent logic by providing
 * methods to advance the time as needed for test cases.
 * </p>
 *
 * <p>
 * Typical usage involves replacing calls to system time with {@code MutableTime}
 * in test environments, enabling deterministic and repeatable tests.
 * </p>
 */
public class MutableTime extends ClockTime {

    private final AtomicLong currentMillis;

    private final ZoneId zoneId;

    private final CopyOnWriteArrayList<MutableTimeListener> listeners = new CopyOnWriteArrayList<>();

    public MutableTime() {
        this(System.currentTimeMillis());
    }

    public MutableTime(long currentMillis) {
        this(currentMillis, ZoneId.systemDefault());
    }

    public MutableTime(ZoneId zoneId) {
        this(System.currentTimeMillis(), zoneId);
    }

    public MutableTime(long currentMillis, ZoneId zoneId) {
        this.currentMillis = new AtomicLong(currentMillis);
        this.zoneId = Objects.requireNonNull(zoneId, "zoneId must not be null");
    }

    @Override
    public ZoneId getZone() {
        return zoneId;
    }

    @Override
    public MutableTime withZone(ZoneId zone) {
        return new MutableTime(this.currentMillis.get(), zone);
    }

    @Override
    public Instant instant() {
        return Instant.ofEpochMilli(currentMillis.get());
    }

    @Override
    public void sleep(long millis) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        this.advance(Duration.ofMillis(millis));
    }
    /**
     * Advances the current time by the specified duration.
     *
     * <p>
     * This method increments the internal time by the given {@link Duration},
     * allowing tests to simulate the passage of time without waiting.
     * </p>
     *
     * <p>
     * The {@code duration} can be negative, which will move the time backwards (rewind).
     * Listeners will be notified whether the time is advanced or rewound.
     * However, using negative durations is not recommended.
     * </p>
     *
     * @param duration the amount of time to advance; must not be null
     */
    public void advance(Duration duration) {
        long now = currentMillis.addAndGet(duration.toMillis());
        for (MutableTimeListener listener : listeners) {
            try {
                listener.onTimeChanged(now);
            } catch (Exception ignored) {
                // Ignore exceptions from listeners to avoid breaking the time advancement
                // This is a design choice to ensure that one listener's failure does not affect others
            }
        }
    }

    /**
     * Registers a listener to be notified whenever the time is advanced or rewound.
     *
     * <p>
     * The listener will be notified in a thread-safe manner after each call to {@link #advance(Duration)},
     * regardless of whether the time moves forward or backward.
     * If the listener is already registered, it will not be added again.
     * </p>
     *
     * @param listener the listener to register; must not be null
     */
    public void addListener(MutableTimeListener listener) {
        listeners.addIfAbsent(listener);
    }

    /**
     * Unregisters a previously registered listener.
     *
     * <p>
     * After removal, the listener will no longer be notified when the time is advanced or rewound.
     * If the listener was not registered, this method has no effect.
     * </p>
     *
     * @param listener the listener to remove; must not be null
     */
    public void removeListener(MutableTimeListener listener) {
        listeners.remove(listener);
    }
}
