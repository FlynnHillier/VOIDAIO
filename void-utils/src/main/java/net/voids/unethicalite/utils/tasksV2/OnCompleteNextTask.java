package net.voids.unethicalite.utils.tasksV2;

import java.util.function.BooleanSupplier;

public class OnCompleteNextTask extends NextTask
{
    protected OnCompleteNextTask(Task task, BooleanSupplier condition)
    {
        super(
                task,
                (Failure.Type i_) -> condition.getAsBoolean()
        );
    }



}
