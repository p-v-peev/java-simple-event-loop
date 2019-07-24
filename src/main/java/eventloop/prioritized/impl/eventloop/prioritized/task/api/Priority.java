package eventloop.prioritized.impl.eventloop.prioritized.task.api;


public enum Priority {
    HIGHEST(0),
    HIGH(1),
    NORMAL(2),
    LOW(3),
    LOWEST(4);

    private final int priority;

    Priority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
