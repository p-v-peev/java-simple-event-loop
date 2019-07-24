package eventloop.prioritized.impl.eventloop.prioritized.task.impl;

import eventloop.prioritized.impl.eventloop.prioritized.task.api.OrderedTaskResult;
import eventloop.prioritized.impl.eventloop.prioritized.task.api.Priority;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderedRunnableTask implements OrderedTaskResult {

    private final Priority priority;
    private final Runnable callback;

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public void processResult() {
        callback.run();
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
