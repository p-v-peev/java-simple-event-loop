package eventloop.prioritized.impl;

import eventloop.api.EventLoop;
import eventloop.api.TaskResult;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public abstract class AbstractEventLoop implements EventLoop {

    protected final ScheduledExecutorService taskExecutor;
    protected final BlockingQueue<TaskResult> finishedTasks;

    private volatile boolean isRunning = false;

    @Override
    public abstract <T> Optional<Future> submit(Callable<T> task, BiConsumer<T, Exception> callback);

    @Override
    public void loop() {
        try {
            isRunning = true;
            while (isRunning) {
                finishedTasks.take().processResult();
            }
        } catch (RuntimeException e) {
            taskExecutor.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void breakLoop() {
        isRunning = false;
        taskExecutor.shutdown();
    }

    @Override
    public List<Runnable> breakLoopNow() {
        isRunning = false;
        return taskExecutor.shutdownNow();
    }
}
