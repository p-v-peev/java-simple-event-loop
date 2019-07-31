package eventloop.api;

import eventloop.impl.SimpleEventLoopImpl;
import eventloop.impl.TimerEventLoopImplImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Static factory which creates event loop implementations
 */
public class EvenLoops {

    public static EventLoop newSimpleLinkedEventLoop() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        return new SimpleEventLoopImpl(executor, new LinkedBlockingQueue<>());
    }

    public static TimerEventLoop newTimerLinkedEventLoop() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        return new TimerEventLoopImplImpl(executor, new LinkedBlockingQueue<>());
    }
}
