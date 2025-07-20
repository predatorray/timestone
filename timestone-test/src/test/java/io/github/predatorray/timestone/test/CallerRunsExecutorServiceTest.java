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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CallerRunsExecutorServiceTest {

    @Test
    void testExecuteUsingSameThread() {
        CallerRunsExecutorService executorService = new CallerRunsExecutorService();
        Thread currentThread = Thread.currentThread();

        CompletableFuture<Thread> completableFuture = new CompletableFuture<>();
        executorService.execute(() -> completableFuture.complete(Thread.currentThread()));

        assertTrue(completableFuture.isDone());
        assertSame(currentThread, completableFuture.join(),
                "The task should run in the calling thread, not in a new thread.");
    }

    @Test
    void testExecutorServiceNotShutdown() {
        CallerRunsExecutorService executorService = new CallerRunsExecutorService();
        assertFalse(executorService.isShutdown(), "Executor service should not be shutdown initially.");
        assertFalse(executorService.isTerminated(), "Executor service should not be terminated initially.");

        executorService.shutdown();
    }

    @Test
    void testShutdown() {
        CallerRunsExecutorService executorService = new CallerRunsExecutorService();
        executorService.shutdown();

        assertThrows(
                RejectedExecutionException.class,
                () -> executorService.execute(() -> {}),
                "Should throw RejectedExecutionException when trying to execute after shutdown.");

        assertTrue(executorService.isShutdown());
        assertTrue(executorService.isTerminated());
    }

    @Test
    void testShutdownNow() {
        CallerRunsExecutorService executorService = new CallerRunsExecutorService();
        List<Runnable> runnables = executorService.shutdownNow();
        assertEquals(0, runnables.size());

        assertThrows(
                RejectedExecutionException.class,
                () -> executorService.execute(() -> {}),
                "Should throw RejectedExecutionException when trying to execute after shutdown.");

        assertTrue(executorService.isShutdown());
        assertTrue(executorService.isTerminated());
    }

    @Test
    void testShutdownButNotTerminated() throws Exception {
        CallerRunsExecutorService executorService = new CallerRunsExecutorService();
        CountDownLatch executedLatch = new CountDownLatch(1);
        Thread executeThread = new Thread(() -> executorService.execute(() -> {
            executedLatch.countDown();
            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));
        executeThread.start();

        try {
            executedLatch.await();
            executorService.shutdown();

            assertTrue(executorService.isShutdown());
            assertFalse(executorService.isTerminated());
        } finally {
            executeThread.interrupt();
            executeThread.join();
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 100})
    void testAwaitTermination(int timeoutMs) throws InterruptedException {
        CallerRunsExecutorService executorService = new CallerRunsExecutorService();
        CountDownLatch executedLatch = new CountDownLatch(1);
        Thread executeThread = new Thread(() -> executorService.execute(() -> {
            executedLatch.countDown();
            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));
        executeThread.start();

        boolean terminated;
        try {
            executedLatch.await();
            executorService.shutdown();

            terminated = executorService.awaitTermination(timeoutMs, TimeUnit.MILLISECONDS);
            assertFalse(terminated);
        } finally {
            executeThread.interrupt();
            executeThread.join();
        }

        terminated = executorService.awaitTermination(timeoutMs, TimeUnit.MILLISECONDS);
        assertTrue(terminated);
    }
}
