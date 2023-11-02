package net.voids.unethicalite.utils.tasks;

import  net.voids.unethicalite.utils.api.Activity;

public abstract class Task
{

    public Activity getActivity()
    {
        return Activity.IDLE;
    }

    public abstract String getStatus();

    public abstract boolean validate();

    public abstract void execute();
}