package net.voids.unethicalite.utils.managers;

import lombok.extern.slf4j.Slf4j;
import net.voids.unethicalite.utils.jobs.Job;

@Slf4j
public abstract class Manager {
    private Job currentJob;
    private Job queuedJob;

    public abstract String getTitle();



    public abstract Job getSuitableJob();


    public Job tick()
    {

        if (currentJob == null)
        {
            currentJob = getSuitableJob();
        }
        else if (queuedJob == null)
        {
            Job nextSuitable = getSuitableJob();

            if (!nextSuitable.getClass().equals(currentJob.getClass()))
            {
                queuedJob = nextSuitable;
            }

        }

        if (queuedJob != null && currentJob.isSafeToEnd())
        {
            currentJob = queuedJob;
            queuedJob = null;
        }

        return currentJob;
    }
}
