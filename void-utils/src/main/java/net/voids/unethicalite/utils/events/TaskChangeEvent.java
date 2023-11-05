package net.voids.unethicalite.utils.events;

import lombok.Getter;
import net.voids.unethicalite.utils.tasks.Task;

public class TaskChangeEvent
{
    @Getter
    private final Task task;

    public TaskChangeEvent(Task currentTask)
    {
        task = currentTask;
    }
}
