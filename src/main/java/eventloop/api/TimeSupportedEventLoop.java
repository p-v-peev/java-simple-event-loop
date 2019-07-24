package eventloop.api;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public interface TimeSupportedEventLoop extends EventLoop {

    Optional<ScheduledFuture> setTimeout(Runnable callback, long delay);

    Optional<ScheduledFuture> setTimeout(Runnable callback, long delay, TimeUnit timeUnit);

    Optional<ScheduledFuture> setInterval(Runnable callback, long period);

    Optional<ScheduledFuture> setInterval(Runnable callback, long initialDelay, long period);

    Optional<ScheduledFuture> setInterval(Runnable callback, long initialDelay, long period, TimeUnit timeUnit);

    <T> Optional<ScheduledFuture> setInterval(Callable<T> task, BiConsumer<T, Exception> callback, long period);

    <T> Optional<ScheduledFuture> setInterval(Callable<T> task, BiConsumer<T, Exception> callback, long period, TimeUnit timeUnit);

    <T> Optional<ScheduledFuture> setInterval(Callable<T> task, BiConsumer<T, Exception> callback, long initialDelay, long period, TimeUnit timeUnit);
}
