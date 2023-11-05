package net.voids.unethicalite.utils.tasks;

import lombok.Getter;
import  net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.jobs.Job;

import javax.inject.Inject;

public abstract class Task
{
    @Getter
    protected Job job;


    @Inject
    public Task(Job job)
    {
        this.job = job;
    }

    @Getter
    private final boolean isSafeToEnd = true;

    @Getter
    private final boolean interruptable = false;


    public Activity getActivity()
    {
        return Activity.IDLE;
    }

    public abstract String getStatus();

    public abstract boolean validate();

    public abstract void execute();
}