package eventloop.prioritized.impl.eventloop.prioritized.task.api;

import eventloop.api.TaskResult;

public interface OrderedTaskResult extends TaskResult, Comparable {
    Priority getPriority();
}
