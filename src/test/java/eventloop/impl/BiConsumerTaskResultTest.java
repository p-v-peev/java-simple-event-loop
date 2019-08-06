package eventloop.impl;

import org.junit.Test;

import java.util.function.BiConsumer;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class BiConsumerTaskResultTest {

    @Test
    public void testCallbackWithException() {
        Exception e = new RuntimeException("TestCallback");
        Object[] callbackArguments = new Object[2];

        BiConsumer<Object, Exception> callback = (data, error) -> {
            callbackArguments[0] = data;
            callbackArguments[1] = error;
        };

        BiConsumerTaskResult taskResult = new BiConsumerTaskResult(null, e, callback);
        taskResult.processResult();

        assertNull("The task result is not null, but should be", callbackArguments[0]);
        assertSame("The exception is not the same as the original one", callbackArguments[1], e);
    }

}