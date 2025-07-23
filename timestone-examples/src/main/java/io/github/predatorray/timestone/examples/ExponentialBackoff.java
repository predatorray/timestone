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

import io.github.predatorray.timestone.Time;

/**
 * An implementation of an exponential backoff strategy.
 * This class provides a way to manage retry delays that increase exponentially
 * with each failure, up to a maximum delay.
 */
public class ExponentialBackoff {

    private final Time time;

    private final long initialDelayMillis;
    private final long maxDelayMillis;
    private final double multiplier;
    private long currentDelayMillis;

    public ExponentialBackoff(Time time, long initialDelayMillis, long maxDelayMillis, double multiplier) {
        if (initialDelayMillis <= 0 || maxDelayMillis <= 0 || multiplier <= 1.0) {
            throw new IllegalArgumentException("Invalid arguments for backoff settings");
        }
        this.time = time;
        this.initialDelayMillis = initialDelayMillis;
        this.maxDelayMillis = maxDelayMillis;
        this.multiplier = multiplier;
        this.currentDelayMillis = initialDelayMillis;
    }

    public void reset() {
        this.currentDelayMillis = initialDelayMillis;
    }

    /**
     * Performs the backoff operation, sleeping for the current delay duration
     * and then increasing the delay for the next call.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    public void backoff() throws InterruptedException {
        time.sleep(currentDelayMillis); // Instead of Thread.sleep for testability
        increaseDelay();
    }

    private void increaseDelay() {
        currentDelayMillis = Math.min((long)(currentDelayMillis * multiplier), maxDelayMillis);
    }
}
