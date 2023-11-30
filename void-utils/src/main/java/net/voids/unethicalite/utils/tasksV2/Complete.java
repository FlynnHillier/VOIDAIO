package net.voids.unethicalite.utils.tasksV2;

public class Complete extends Task
{
    public Complete()
    {
        super("completion");
    }

    @Override
    public boolean completionCondition()
    {
        return true;
    }

    @Override
    protected int execute()
    {
        return 0;
    }

    @Override
    protected void onInitialise()
    {
    }
}
