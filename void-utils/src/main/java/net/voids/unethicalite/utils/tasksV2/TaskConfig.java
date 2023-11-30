package net.voids.unethicalite.utils.tasksV2;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * Configuration for tasks that may need to change between implementations of the task.
 */
public class TaskConfig
{
    protected int sleepOnComplete = 0;

    protected boolean loopUntilSomeNextTaskShouldRun = false;

    private final ArrayList<NextTask> onCompleteNextTasks = new ArrayList<>();

    private final ArrayList<NextTask> onFailNextTasks = new ArrayList<>();


    public TaskConfig loopUntilSomeNextTaskShouldRun(boolean bool)
    {
        this.loopUntilSomeNextTaskShouldRun = bool;
        return this;
    }

    public TaskConfig sleepOnComplete(int sleepPeriod)
    {
        this.sleepOnComplete = sleepPeriod;
        return this;
    }


    /**
     *
     * @param task - the task that should run if the specified fail-type occurs.
     * @param failTypeToRunOn - the failure type that should trigger the task to run.
     * @return this
     */
    public TaskConfig nextTaskOnFail(Task task, Failure.Type failTypeToRunOn)
    {
        this.onFailNextTasks.add(NextTask.onFail(task, failTypeToRunOn));

        return this;
    }

    /**
     *
     * @param task the next task that should run
     * @return this
     */
    public TaskConfig nextTaskOnComplete(Task task)
    {
        return nextTaskOnComplete(task, () -> true);
    }


    /**
     *
     * @param task the next task that should run
     * @param condition a condition that must be satisfied for this task to run
     * @return this
     */
    public TaskConfig nextTaskOnComplete(Task task, BooleanSupplier condition)
    {
        this.onCompleteNextTasks.add(NextTask.onComplete(task, condition));

        return this;
    }

    /**
     *
     * @return Optional<Task> That should run after this task has been completed, based on the conditions specified. Will return Optional.empty() if no suitable task is available.
     */
    protected Optional<Task> onCompleteGetNextTask()
    {
        for (NextTask someNextTask : onCompleteNextTasks)
        {
            if (someNextTask.shouldRun())
            {
                return Optional.of(someNextTask.get());
            }
        }

        return Optional.empty();
    }

    /**
     *
     * @return Optional<Task> That should run after this task has failed, based on the conditions specified. Will return Optional.empty() if no suitable task is available.
     */
    protected Optional<Task> onFailGetNextTask(Failure.Type failType)
    {
        for (NextTask someNextTask : onFailNextTasks)
        {
            if (someNextTask.shouldRun(failType))
            {
                return Optional.of(someNextTask.get());
            }
        }

        return Optional.empty();
    }
}
