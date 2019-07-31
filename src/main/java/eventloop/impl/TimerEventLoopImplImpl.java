package eventloop.impl;

import eventloop.api.TimerEventLoop;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

@Slf4j
public class TimerEventLoopImplImpl extends SimpleEventLoopImpl implements TimerEventLoop {

    private final ScheduledExecutorService taskExecutor;

    public TimerEventLoopImplImpl(ScheduledExecutorService taskExecutor, BlockingQueue<TaskResult> finishedTasks) {
        super(taskExecutor, finishedTasks);
        this.taskExecutor = taskExecutor;

    }

    @Override
    public Optional<ScheduledFuture> setTimeout(Runnable callback, long delay) {
        return setTimeout(callback, delay, TimeUnit.MILLISECONDS);
    }


    @Override
    public Optional<ScheduledFuture> setTimeout(Runnable callback, long delay, TimeUnit timeUnit) {
        try {
            return Optional.of(taskExecutor.schedule(() -> {
                TaskResult result = new RunnableTaskResult(callback);
                try {
                    finishedTasks.put(result);
                } catch (InterruptedException e) {
                    log.error("Failed to add the callback function: {} for processing in the event loop.It will be never invoked.", callback, e);
                }
            }, delay, timeUnit));
        } catch (RejectedExecutionException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ScheduledFuture> setInterval(Runnable callback, long period) {
        return setInterval(callback, 0L, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public Optional<ScheduledFuture> setInterval(Runnable callback, long initialDelay, long period) {
        return setInterval(callback, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public Optional<ScheduledFuture> setInterval(Runnable callback, long initialDelay, long period, TimeUnit timeUnit) {
        try {
            return Optional.of(taskExecutor.scheduleAtFixedRate(() -> {
                TaskResult result = new RunnableTaskResult(callback);
                try {
                    finishedTasks.put(result);
                } catch (InterruptedException e) {
                    log.error("Failed to add the result from task: {} for processing in the event loop. The callback function: {} will be never invoked.", callback, callback, e);
                }
            }, initialDelay, period, timeUnit));
        } catch (RejectedExecutionException e) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<ScheduledFuture> setInterval(Callable<T> task, BiConsumer<T, Exception> callback, long period) {
        return setInterval(task, callback, 0, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> Optional<ScheduledFuture> setInterval(Callable<T> task, BiConsumer<T, Exception> callback, long period, TimeUnit timeUnit) {
        return setInterval(task, callback, 0, period, timeUnit);
    }

    @Override
    public <T> Optional<ScheduledFuture> setInterval(Callable<T> task, BiConsumer<T, Exception> callback, long initialDelay, long period, TimeUnit timeUnit) {
        try {
            return Optional.of(taskExecutor.scheduleAtFixedRate(() -> {
                TaskResult result;
                try {
                    T taskResult = task.call();
                    result = new BiConsumerTaskResult(taskResult, null, (BiConsumer<Object, Exception>) callback);
                } catch (Exception e) {
                    result = new BiConsumerTaskResult(null, e, (BiConsumer<Object, Exception>) callback);
                }

                try {
                    finishedTasks.put(result);
                } catch (InterruptedException e) {
                    log.error("Failed to add the result from task: {} for processing in the event loop. The callback function: {} will be never invoked.", callback, callback, e);
                }
            }, initialDelay, period, timeUnit));
        } catch (RejectedExecutionException e) {
            return Optional.empty();
        }
    }
}
