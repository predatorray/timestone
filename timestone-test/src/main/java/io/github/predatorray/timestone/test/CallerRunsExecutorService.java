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

import io.github.predatorray.timestone.Time;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * An {@link java.util.concurrent.ExecutorService} implementation where tasks are executed
 * on the calling thread, similar to the {@link java.util.concurrent.Executors#newSingleThreadExecutor()}
 * but without a background thread.
 *
 * <p>This implementation maintains a count of currently running tasks and provides proper support for
 * shutdown and await termination. It is useful in scenarios such as testing or when using threadless
 * execution environments.
 *
 * <p>When a task is submitted, it runs immediately in the thread that invokes {@code execute()}.
 * If the executor has been shut down, task submission will result in a {@link RejectedExecutionException}.
 */
public class CallerRunsExecutorService extends AbstractExecutorService {

    private final Time time;

    private final Object runningTasksMonitor = new Object();
    private int runningTasks = 0;

    private volatile boolean shutdown = false;

    /**
     * Creates a new instance using the system time.
     */
    public CallerRunsExecutorService() {
        this(Time.SYSTEM);
    }

    /**
     * Creates a new instance using a provided {@link Time} instance.
     *
     * @param time the {@code Time} source used for measuring elapsed time during {@code awaitTermination}
     * @throws NullPointerException if {@code time} is null
     */
    public CallerRunsExecutorService(Time time) {
        this.time = Objects.requireNonNull(time);
    }

    /**
     * Executes the given command immediately in the calling thread.
     *
     * @param command the runnable task
     * @throws RejectedExecutionException if the executor has been shut down
     */
    @Override
    public void execute(Runnable command) {
        rejectIfShutdown();

        FutureTask<Void> runningTask = new FutureTask<>(command, null);
        synchronized (runningTasksMonitor) {
            runningTasks++;
            runningTasksMonitor.notifyAll();
        }
        try {
            runningTask.run();
        } finally {
            synchronized (runningTasksMonitor) {
                runningTasks--;
                runningTasksMonitor.notifyAll();
            }
        }
    }

    private void rejectIfShutdown() {
        if (shutdown) {
            throw new RejectedExecutionException("Executor service is shutdown");
        }
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }

    /**
     * Initiates an immediate shutdown. No new tasks will be accepted.
     * Since tasks run immediately on the calling thread, this implementation
     * returns an empty list.
     *
     * @return an empty list as no tasks are queued
     */
    @Override
    public List<Runnable> shutdownNow() {
        shutdown();
        return Collections.emptyList();
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public boolean isTerminated() {
        synchronized (runningTasksMonitor) {
            return isShutdown() && runningTasks == 0;
        }
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long timeoutMsLeft = unit.toMillis(timeout);
        synchronized (runningTasksMonitor) {
            while (runningTasks > 0 && timeoutMsLeft > 0) {
                long startTime = time.millis();
                runningTasksMonitor.wait(timeoutMsLeft);
                long elapsedTime = time.millis() - startTime;
                timeoutMsLeft -= elapsedTime;
            }
            return runningTasks == 0;
        }
    }
}
