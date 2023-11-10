package net.voids.unethicalite.cowkill.tasks;

import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

public class CloseBank extends Task
{
    public CloseBank(Job job)
    {
        super(job);
    }


    @Override
    public Activity getActivity()
    {
        return null;
    }

    @Override
    public String getStatus()
    {
        return "closing bank";
    }

    @Override
    public boolean validate()
    {
        return Bank.isOpen();
    }

    @Override
    public void execute()
    {
        Time.sleepTick();
        Bank.close();
    }
}
