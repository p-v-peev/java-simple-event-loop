package eventloop.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class RunnableTaskResult implements TaskResult {

    private final Runnable callback;

    @Override
    public void processResult() {
        callback.run();
    }

}
