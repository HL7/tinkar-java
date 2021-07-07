package org.hl7.tinkar.common.util.thread;

import java.util.concurrent.Semaphore;

/**
 * Class to manage multi-threaded tasks, to ensure number of
 * concurrent tasks do not overwhelm queue.
 */
public class TaskCountManager {


    /**
     * Use when multi threading a task, to ensure that queue resources don't get overwhelmed.
     * Search for usages for examples. Semaphore count is from the permitCount() method on this class.
     * @return a Semaphore for governing task execution.
     */
    public static TaskCountManager get() {
        return new TaskCountManager();
    }

    private final int taskCount;
    private final Semaphore taskSemaphore;

    public TaskCountManager(int taskCount) {
        this.taskCount = taskCount;
        this.taskSemaphore = new Semaphore(taskCount);
    }

    /**
     * Provides a standard manager for concurrent additions to queues for multi-threaded tasks. The size prevents
     * the queues from being overwhelmed, but also is large enough to keep the CPU occupied.
     * @return TaskCountManager with count = Runtime.getRuntime().availableProcessors() * 2
     */
    public TaskCountManager() {
        this(Runtime.getRuntime().availableProcessors() * 2);
    }

    public void acquire() throws InterruptedException {
        this.taskSemaphore.acquire();
    }
    public void release() {
        this.taskSemaphore.release();
    }

    public void waitForCompletion() throws InterruptedException {
        this.taskSemaphore.acquire(taskCount);
        this.taskSemaphore.release(taskCount);
    }

}
