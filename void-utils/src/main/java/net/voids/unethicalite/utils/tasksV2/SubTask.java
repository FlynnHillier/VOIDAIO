package net.voids.unethicalite.utils.tasksV2;

import kotlin.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

public class SubTask
{
    @Getter
    private final Task task;

    @Getter
    private boolean loopUntilSomeValidNext = false;


    List<NextTask> nextTasks = new ArrayList<>();
    List<Pair<SubTask, Failure.Type>> handleFailTask = new ArrayList<>();


    public SubTask(Task task)
    {
        this.task = task;
    }


    public SubTask next(SubTask task)
    {
        return next(task, () -> true);
    }

    public SubTask next(SubTask task, BooleanSupplier condition)
    {
        return next(task, condition, 0);
    }

    public SubTask next(SubTask task, int sleepOnComplete)
    {
        return next(task, () -> true, sleepOnComplete);
    }

    public SubTask next(SubTask task, BooleanSupplier condition, int sleepOnComplete)
    {
        nextTasks.add(new NextTask(task, condition, sleepOnComplete));

        return this;
    }

    public SubTask loopUntilSomeValidNext()
    {
        loopUntilSomeValidNext = true;

        return this;
    }


    public SubTask onFail(Failure.Type type, Task task)
    {


        return this;
    }

    public Optional<NextTask> getNextTask()
    {
        for (NextTask nextTask : nextTasks)
        {
            if (nextTask.getCondition().getAsBoolean())
            {
                return Optional.of(nextTask);
            }
        }

        return Optional.empty();
    }

    public Optional<SubTask> getHandleFailureTask(Failure.Type fail)
    {
        for (Pair<SubTask, Failure.Type> failTask : handleFailTask)
        {
            if (failTask.getSecond().equals(fail))
            {
                return Optional.of(failTask.getFirst());
            }
        }

        return Optional.empty();
    }
}
