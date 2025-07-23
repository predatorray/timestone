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

import java.time.Duration;
import java.time.Instant;

/**
 * A simple stopwatch implementation that measures elapsed time.
 * It uses a provided {@link Time} instance to get the current time.
 */
public class StopWatch {

    private final Time time;

    private Instant startInstant;
    private Instant endInstant;

    public StopWatch(Time time) {
        this.time = time;
    }

    public void start() {
        this.startInstant = this.time.instant();
        this.endInstant = null;
    }

    public void stop() {
        if (this.startInstant == null) {
            throw new IllegalArgumentException("Stopwatch has not been started.");
        }
        this.endInstant = this.time.instant();
    }

    public Duration getElapsedTime() {
        if (this.startInstant == null || this.endInstant == null) {
            throw new IllegalArgumentException("Stopwatch has not been started or stopped properly.");
        }
        return Duration.between(startInstant, endInstant);
    }
}
