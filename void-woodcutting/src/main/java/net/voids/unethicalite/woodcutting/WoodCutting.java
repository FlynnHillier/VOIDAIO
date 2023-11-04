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
        addTask(new Equip());
        addTask(new Drop());
        addTask(new TravelToArea());
        addTask(new Chop());
    }


    @Override
    public String getTitle() {
        return "woodcutting";
    }


}