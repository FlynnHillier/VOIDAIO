package net.voids.unethicalite.cowkill;

import net.voids.unethicalite.cowkill.tasks.*;
import net.voids.unethicalite.utils.jobs.Job;

class CowKiller extends Job
{
    @Override
    public String getTitle()
    {
        return "cow killer";
    }

    public CowKiller()
    {
        addTask(new Eat(this));
        addTask(new Deposit(this));
        addTask(new Withdraw(this));
        addTask(new CloseBank(this));
        addTask(new CookBeef(this));
        addTask(new Loot(this));
        addTask(new AttackCow(this));
    }

}