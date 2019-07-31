package examples;

import eventloop.api.EvenLoops;
import eventloop.api.TimerEventLoop;

public class Main {
    public static void main(String[] args) {
        TimerEventLoop loop = EvenLoops.newTimerLinkedEventLoop();
        loop.setInterval(() -> "TEST", (data, error) -> System.out.println(data), 1000);

        loop.setLoopThreshold(5);

        loop.startLoop();
    }
}
