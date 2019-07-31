package eventloop.api;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

/**
 * Basic definition of the event loop.
 */
public interface EventLoop {

    /**
     * Submits a task for asynchronous execution and then invokes the callback with the result from that task,
     * in the loop thread.
     *
     * @param task     the task which needs to be executed asynchronously
     * @param callback the function which will be executed in the loop thread
     * @param <T>      the type of the result which the task returns
     * @return an {@link Optional} which contains {@link Future} to cancel the task or {@link Optional#empty()}
     * if the task submission has failed
     */
    <T> Optional<Future> submit(Callable<T> task, BiConsumer<T, Exception> callback);

    /**
     * Starts the processing of the finished asynchronous tasks.
     */
    void startLoop();

    /**
     * Sets a threshold for the amount of time which the loop thread needs to complete one cycle.
     * If the amount if time is greater than the threshold value, the implementations must warn
     * that some operation takes too much time in the loop thread.
     *
     * @param threshold the time in which the loop thread must make one execution of the event loop
     */
    void setLoopThreshold(long threshold);

    /**
     * Causes the loop thread to exit and shuts down the worker threads.
     *
     * @return the tasks which were waiting for execution;
     */
    List<Runnable> breakLoop();
}
