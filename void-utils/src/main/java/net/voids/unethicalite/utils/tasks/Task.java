package net.voids.unethicalite.utils.tasks;

import lombok.Getter;
import  net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.jobs.Job;

import javax.inject.Inject;
import java.util.ArrayList;

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
    protected ArrayList<Class<? extends Task>> interruptableBy = new ArrayList<>();


    public final boolean isInterruptable()
    {
        return !interruptableBy.isEmpty();
    }


    public Activity getActivity()
    {
        return Activity.IDLE;
    }

    public abstract String getStatus();

    public abstract boolean validate();

    public abstract void execute();
}