package eventloop.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.function.BiConsumer;

@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class BiConsumerTaskResult implements TaskResult {

    private final Object result;
    private final Exception exception;
    private final BiConsumer<Object, Exception> callback;

    @Override
    public void processResult() {
        callback.accept(result, exception);
    }

}
