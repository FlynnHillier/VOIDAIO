package net.voids.unethicalite.aio.managers;

import lombok.extern.slf4j.Slf4j;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.managers.Manager;
import net.voids.unethicalite.woodcutting.WoodCutting; //fails here

@Slf4j
public class SkillManager extends Manager
{

    //Have a list of jobs.
    //Sort said list of jobs.
    //When you fetch a job compare skills against only next job (next compared to currentJob)
    //if nextJob is now available, check it is safe to end currentJob, if so updated currentJob to new job


    @Override
    public String getTitle()
    {
        return "skilling";
    }

    @Override
    public Job getSuitableJob()
    {
        WoodCutting w = new WoodCutting();
        return w;
    }
}
