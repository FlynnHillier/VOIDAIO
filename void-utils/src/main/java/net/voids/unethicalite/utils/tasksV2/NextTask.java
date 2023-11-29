package net.voids.unethicalite.utils.tasksV2;

import lombok.Getter;

import java.util.function.BooleanSupplier;

public class NextTask
{
    @Getter
    private final SubTask subTask;

    @Getter
    private final BooleanSupplier condition;

    @Getter
    private final int sleepOnComplete;


    public NextTask(SubTask task, BooleanSupplier condition, int sleepOnComplete)
    {
        this.subTask = task;
        this.condition = condition;
        this.sleepOnComplete = sleepOnComplete;
    }
}
