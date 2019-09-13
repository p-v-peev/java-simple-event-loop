package eventloop.impl;

import eventloop.api.EventLoop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

@Slf4j
@RequiredArgsConstructor
public class SimpleEventLoopImpl implements EventLoop {

    private volatile boolean isRunning = false;
    private volatile long threshold = Long.MAX_VALUE;

    protected final ExecutorService taskExecutor;
    protected final BlockingQueue<TaskResult> finishedTasks;

    @Override

    public <T> Optional<Future> submit(Callable<T> task, BiConsumer<T, Exception> callback) {
        try {
            return Optional.of(taskExecutor.submit(() -> {
                TaskResult result;
                try {
                    T taskResult = task.call();
                    result = new BiConsumerTaskResult<>(taskResult, null, callback);
                } catch (Exception e) {
                    result = new BiConsumerTaskResult<>(null, e, callback);
                }
                try {
                    finishedTasks.put(result);
                } catch (InterruptedException e) {
                    log.error("Failed to add the result from task: {} for processing in the event loop. The callback function: {} will be never invoked.", task, callback, e);
                }
            }));
        } catch (RejectedExecutionException e) {
            return Optional.empty();
        }
    }

    @Override
    public void startLoop() {
        try {
            isRunning = true;
            while (isRunning) {
                TaskResult task = finishedTasks.take();
                long startOfExecution = System.currentTimeMillis();
                task.processResult();
                long endOfExecution = System.currentTimeMillis();
                long executionTime = endOfExecution - startOfExecution;
                if (executionTime > threshold) {
                    log.warn("Processing of task {} result, took {} milliseconds which is more that the defined threshold of {} milliseconds.", task, executionTime, threshold);
                }
            }
        } catch (RuntimeException | InterruptedException e) {
            taskExecutor.shutdownNow();
            e.printStackTrace();
        }
    }

    @Override
    public void setLoopThreshold(long threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException("The threshold value must be not negative.");
        }
        this.threshold = threshold;
    }

    @Override
    public List<Runnable> breakLoop() {
        isRunning = false;
        return taskExecutor.shutdownNow();
    }
}
