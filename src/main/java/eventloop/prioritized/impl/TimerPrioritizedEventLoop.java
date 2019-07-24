package eventloop.prioritized.impl;

import eventloop.api.TaskResult;
import eventloop.api.TimeSupportedEventLoop;
import eventloop.prioritized.impl.eventloop.prioritized.task.api.Priority;
import eventloop.prioritized.impl.eventloop.prioritized.task.impl.OrderedBiConsumerTaskResult;
import eventloop.prioritized.impl.eventloop.prioritized.task.impl.OrderedRunnableTask;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

public class TimerPrioritizedEventLoop extends AbstractEventLoop implements TimeSupportedEventLoop {


    public TimerPrioritizedEventLoop() {
        super(Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() + 1), new PriorityBlockingQueue<>());
    }

    @Override
    public <T> Optional<Future> submit(Callable<T> task, BiConsumer<T, Exception> callback) {
        Future result = null;
        try {
            result = taskExecutor.submit(() -> {
                try {
                    T taskResult = task.call();
                    finishedTasks.offer(new OrderedBiConsumerTaskResult(Priority.NORMAL, taskResult, null, (BiConsumer<Object, Exception>) callback));
                } catch (Exception e) {
                    finishedTasks.offer(new OrderedBiConsumerTaskResult(Priority.NORMAL, null, e, (BiConsumer<Object, Exception>) callback));
                }
            });
        } catch (RejectedExecutionException e) {
            finishedTasks.offer(new OrderedBiConsumerTaskResult(Priority.NORMAL, null, e, (BiConsumer<Object, Exception>) callback));
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<ScheduledFuture> setTimeout(Runnable callback, long delay) {
        return setTimeout(callback, delay, TimeUnit.MILLISECONDS);
    }


    @Override
    public Optional<ScheduledFuture> setTimeout(Runnable callback, long delay, TimeUnit timeUnit) {
        try {
            return Optional.of(taskExecutor.schedule(() -> {
                finishedTasks.offer(new OrderedRunnableTask(Priority.HIGHEST, callback));
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
            return Optional.of(taskExecutor.scheduleAtFixedRate(() ->
                            finishedTasks.offer(new OrderedRunnableTask(Priority.HIGHEST, callback))
                    , initialDelay, period, timeUnit));
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
        ScheduledFuture result = null;
        try {
            result = taskExecutor.scheduleAtFixedRate(() -> {
                try {
                    T taskResult = task.call();
                    finishedTasks.offer(new OrderedBiConsumerTaskResult(Priority.HIGHEST, taskResult, null, (BiConsumer<Object, Exception>) callback));
                } catch (Exception e) {
                    finishedTasks.offer(new OrderedBiConsumerTaskResult(Priority.HIGHEST, null, e, (BiConsumer<Object, Exception>) callback));
                }
            }, initialDelay, period, timeUnit);
        } catch (RejectedExecutionException e) {
            finishedTasks.offer(new OrderedBiConsumerTaskResult(Priority.HIGHEST, null, e, (BiConsumer<Object, Exception>) callback));
        }
        return Optional.ofNullable(result);
    }
}
