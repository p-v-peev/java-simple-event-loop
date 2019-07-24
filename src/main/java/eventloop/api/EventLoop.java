package eventloop.api;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

public interface EventLoop {

    /**
     * Submits a task to the the underlying pool of threads for asynchronous execution.
     * When the task is done the callback will be invoked either with the result or with
     * the error from the task execution or the task scheduling if any.
     *
     * @param task     the task which needs to e executed asynchronously
     * @param callback the callback which needs to be invoked with the result of the task execution
     * @param <T>      the type of the result which the task returns
     * @return a optional which has to be empty if an error has occurred when the task is submitted for execution
     * or containing a future which can be used to cancel the task.
     */
    <T> Optional<Future> submit(Callable<T> task, BiConsumer<T, Exception> callback);

    void loop();

    void breakLoop();

    List<Runnable> breakLoopNow();

}
