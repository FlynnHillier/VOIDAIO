package net.voids.unethicalite.utils.managers;

import net.voids.unethicalite.utils.jobs.Job;

public abstract class Manager {
    private Job currentJob;

    public abstract String getTitle();


    public abstract Job getSuitableJob();


    public void tick()
    {
        if (currentJob == null || currentJob.isSafeToEnd())
        {
            currentJob = getSuitableJob();
            //check for if some new job is available
        }

        currentJob.tick();
    }
}
