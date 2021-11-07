package org.hl7.tinkar.provider.executor;

import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.hl7.tinkar.common.util.thread.PausableThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class TinkarThreadPoolExecutor extends PausableThreadPoolExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(TinkarThreadPoolExecutor.class);

    public TinkarThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public TinkarThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public TinkarThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public TinkarThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        if (runnable instanceof TrackingCallable trackingCallable) {
            //return TaskWrapper.make(trackingCallable);
        }
        return super.newTaskFor(runnable, value);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof TrackingCallable trackingCallable) {
//            TaskWrapper<T> taskWrapper = TaskWrapper.make(trackingCallable);
//            Platform.runLater(() -> KometExecutorService.pendingTasks.add(taskWrapper));
//            return taskWrapper;
        }
        return super.newTaskFor(callable);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t != null) {
            AlertStreams.getRoot().dispatch(AlertObject.makeError(t));
        }
    }
}