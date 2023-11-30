package net.voids.unethicalite.utils.tasksV2;

import lombok.Getter;

public class Failure
{
    @Getter
    private Type type;
    @Getter
    private String message;

    @Getter
    private Task resolveToTask;


    public enum Type
    {
        TIMEOUT,
        NO_NEXT_TASK,
        NOT_FOUND,
        CANNOT_REACH,
        NO_SUCH_ACTION,
        PATHING,
        WIDGET_NOT_PRESENT,
    }



    public Failure(Type type, String message, Task resolveToTask)
    {
        this.type = type;
        this.message = message;
        this.resolveToTask = resolveToTask;
    }
}
