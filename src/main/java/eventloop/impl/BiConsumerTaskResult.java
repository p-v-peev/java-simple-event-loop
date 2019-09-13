package eventloop.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.function.BiConsumer;

@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class BiConsumerTaskResult<T> implements TaskResult {

    private final T result;
    private final Exception exception;
    private final BiConsumer<T, Exception> callback;

    @Override
    public void processResult() {
        callback.accept(result, exception);
    }

}
