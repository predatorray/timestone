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

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StopWatchTest {

    @Test
    void testStopWatch() {
        MutableTime time = new MutableTime();
        StopWatch stopWatch = new StopWatch(time);
        stopWatch.start();

        // Simulate some work
        Duration elapsed = Duration.ofHours(1);
        time.advance(elapsed);

        stopWatch.stop();

        Duration actualElapsedTime = stopWatch.getElapsedTime();
        assertEquals(elapsed, actualElapsedTime, "Elapsed time should match the simulated duration");
    }
}
