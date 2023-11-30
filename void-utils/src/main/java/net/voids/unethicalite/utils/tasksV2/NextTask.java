package net.voids.unethicalite.utils.tasksV2;

import lombok.Builder;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class NextTask
{
    private final Task task;

    @Builder.Default
    private final Function<Failure.Type, Boolean> condition;


    public static OnFailNextTask onFail(Task task, Failure.Type failTypeToRunOn)
    {
        return new OnFailNextTask(
                task,
                failTypeToRunOn
        );
    }

    public static OnCompleteNextTask onComplete(Task task)
    {
        //default condition (always run)
        return onComplete(task, () -> true);
    }


    public static OnCompleteNextTask onComplete(Task task, BooleanSupplier condition)
    {
        return new OnCompleteNextTask(
                task,
                condition
        );
    }


    protected NextTask(Task task, Function<Failure.Type, Boolean> condition)
    {
        this.task = task;
        this.condition = condition;
    }


    public boolean shouldRun()
    {
        return shouldRun(null);
    }

    public boolean shouldRun(Failure.Type failType)
    {
        return condition.apply(failType);
    }


    public Task get()
    {
        return task;
    }
}
