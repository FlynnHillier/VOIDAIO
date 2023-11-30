package net.voids.unethicalite.utils.tasksV2;

public class OnFailNextTask extends NextTask
{
    protected OnFailNextTask(Task task, Failure.Type failType)
    {
        super(
                task,
                (Failure.Type query) -> query.equals(failType)
        );
    }
}
