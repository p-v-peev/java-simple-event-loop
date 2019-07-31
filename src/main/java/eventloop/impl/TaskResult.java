package eventloop.impl;

/**
 * Class which provides abstraction over the different types of tasks.
 */
interface TaskResult {

    /**
     * Processes the result, from asynchronous task execution, in the loop thread.
     */
    void processResult();
}
