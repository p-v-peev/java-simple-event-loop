package eventloop.prioritized.impl.eventloop.prioritized.task.impl;

import eventloop.prioritized.impl.eventloop.prioritized.task.api.OrderedTaskResult;
import eventloop.prioritized.impl.eventloop.prioritized.task.api.Priority;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;
import java.util.function.BiConsumer;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderedBiConsumerTaskResult implements OrderedTaskResult {

    private final Priority priority;
    private final Object result;
    private final Exception exception;
    private final BiConsumer<Object, Exception> callback;

    @Override
    public void processResult() {
        callback.accept(result, exception);
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Object o) {
        if (Objects.isNull(o)) {
            throw new NullPointerException("Cannot compare to null");
        } else if (!(o instanceof OrderedTaskResult)) {
            throw new IllegalArgumentException("Cannot compare tasks without priority");
        }
        return ((OrderedTaskResult) o).getPriority().getPriority() - this.priority.getPriority();
    }
}
