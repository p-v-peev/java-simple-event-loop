package eventloop.api;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * Provides support for time scheduled operations in the event loop.
 */
public interface TimerEventLoop extends EventLoop {

    /**
     * Delays the execution of the task with given delay in milliseconds.
     * This method guaranties that the execution will be delayed with at least that many milliseconds,
     * but does not guaranties exact delay.
     * The implementation will do its best to execute the task with delay as closer as possible to that delay.
     *
     * @param callback      the function which will be executed in the loop thread
     * @param delayInMillis the amount of time, in milliseconds, to delay the task execution
     * @return an {@link Optional} which contains {@link Future} to cancel the task or {@link Optional#empty()}
     * if the task submission has failed
     */
    Optional<ScheduledFuture> setTimeout(Runnable callback, long delayInMillis);

    /**
     * Delays the execution of the task with given delay in given {@link TimeUnit}.
     * This method guaranties that the execution will be delayed with at least that many units of time,
     * but does not guaranties exact delay.
     * The implementation will do its best to execute the task with delay as closer as possible to that delay.
     *
     * @param callback the function which will be executed in the loop thread
     * @param delay    the amount of time to delay the task execution
     * @param timeUnit the unit for the delay parameter
     * @return an {@link Optional} which contains {@link Future} to cancel the task or {@link Optional#empty()}
     * if the task submission has failed
     */
    Optional<ScheduledFuture> setTimeout(Runnable callback, long delay, TimeUnit timeUnit);

    /**
     * Submits the task for periodical execution with given period in milliseconds.
     * This method guaranties that the execution period will be at least that long.
     * The implementation will do its best to execute the task with period as closer as possible to required period.
     *
     * @param callback       the function which will be executed periodically in the loop thread
     * @param periodInMillis the period, in milliseconds, of the task execution
     * @return an {@link Optional} which contains {@link Future} to cancel the task or {@link Optional#empty()}
     * if the task submission has failed
     */
    Optional<ScheduledFuture> setInterval(Runnable callback, long periodInMillis);

    /**
     * Submits the task for periodical execution with given period and given initial delay in milliseconds.
     * This method guaranties that the execution period will be at least that long.
     * The implementation will do its best to execute the task with period as closer as possible to required period.
     *
     * @param callback       the function which will be executed periodically in the loop thread
     * @param initialDelay   the initial delay before the first task execution
     * @param periodInMillis the period of the subsequent task executions
     * @return an {@link Optional} which contains {@link Future} to cancel the task or {@link Optional#empty()}
     * if the task submission has failed
     */
    Optional<ScheduledFuture> setInterval(Runnable callback, long initialDelay, long periodInMillis);

    /**
     * Submits the task for periodical execution with given period and given initial delay in given {@link TimeUnit}.
     * This method guaranties that the execution period will be at least that long.
     * The implementation will do its best to execute the task with period as closer as possible to required period.
     *
     * @param callback     the function which will be executed periodically in the loop thread
     * @param initialDelay the initial delay before the first task execution
     * @param period       the period of the subsequent task executions
     * @param timeUnit     the unit for the initial delay and the period
     * @return an {@link Optional} which contains {@link Future} to cancel the task or {@link Optional#empty()}
     * if the task submission has failed
     */
    Optional<ScheduledFuture> setInterval(Runnable callback, long initialDelay, long period, TimeUnit timeUnit);

    /**
     * Submits a task for asynchronous execution with a given period in milliseconds and then invokes the callback
     * with the result from that task, in the loop thread.
     * This method guaranties that the execution period will be at least that long.
     * The implementation will do its best to execute the task with period as closer as possible to required period.
     *
     * @param task           the task which needs to be executed asynchronously
     * @param callback       the function which will be executed in the loop thread
     * @param periodInMillis the period of the subsequent task executions
     * @param <T>            the type of the result which the task returns
     * @return an {@link Optional} which contains {@link Future} to cancel the task or {@link Optional#empty()}
     * * if the task submission has failed
     */
    <T> Optional<ScheduledFuture> setInterval(Callable<T> task, BiConsumer<T, Exception> callback, long periodInMillis);

    /**
     * Submits a task for asynchronous execution with a given period in the given time unit and then invokes the callback
     * with the result from that task, in the loop thread.
     * This method guaranties that the execution period will be at least that long.
     * The implementation will do its best to execute the task with period as closer as possible to required period.
     *
     * @param task     the task which needs to be executed asynchronously
     * @param callback the function which will be executed in the loop thread
     * @param period   the period of the subsequent task executions
     * @param timeUnit the unit for the period
     * @param <T>      the type of the result which the task returns
     * @return an {@link Optional} which contains {@link Future} to cancel the task or {@link Optional#empty()}
     * if the task submission has failed
     */
    <T> Optional<ScheduledFuture> setInterval(Callable<T> task, BiConsumer<T, Exception> callback, long period, TimeUnit timeUnit);

    /**
     * Submits a task for asynchronous execution with a given period and initial delay in the given time unit and then invokes the callback
     * with the result from that task, in the loop thread.
     * This method guaranties that the execution period will be at least that long.
     * The implementation will do its best to execute the task with period as closer as possible to required period.
     *
     * @param task         the task which needs to be executed asynchronously
     * @param callback     the function which will be executed in the loop thread
     * @param initialDelay the initial delay
     * @param period       the period of the subsequent task executions
     * @param timeUnit     the unit for the period
     * @param <T>          the type of the result which the task returns
     * @return an {@link Optional} which contains {@link Future} to cancel the task or {@link Optional#empty()}
     * if the task submission has failed
     */
    <T> Optional<ScheduledFuture> setInterval(Callable<T> task, BiConsumer<T, Exception> callback, long initialDelay, long period, TimeUnit timeUnit);
}
