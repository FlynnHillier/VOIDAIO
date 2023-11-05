package net.voids.unethicalite.woodcutting;

import lombok.extern.slf4j.Slf4j;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.woodcutting.tasks.Chop;
import net.voids.unethicalite.woodcutting.tasks.Drop;
import net.voids.unethicalite.woodcutting.tasks.Equip;
import net.voids.unethicalite.woodcutting.tasks.TravelToArea;

import javax.inject.Singleton;

@Slf4j
@Singleton
public class WoodCutting extends Job
{
    public WoodCutting()
    {
        addTask(new Equip(this));
        addTask(new Drop(this));
        addTask(new TravelToArea(this));
        addTask(new Chop(this));
    }


    @Override
    public String getTitle()
    {
        return "woodcutting";
    }
}