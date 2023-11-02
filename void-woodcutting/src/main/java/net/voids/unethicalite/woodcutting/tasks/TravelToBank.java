package net.voids.unethicalite.woodcutting.tasks;

import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.voids.unethicalite.utils.tasks.Task;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import net.voids.unethicalite.woodcutting.WoodCutting;

import javax.inject.Inject;

public class TravelToBank extends Task
{

    @Inject
    WoodCutting plugin;

    @Override
    public String getStatus()
    {
        return "going to bank.";
    }

    @Override
    public boolean validate()
    {
        return Inventory.isFull()
                && BankLocation.getNearest() != null;
    }

    @Override
    public void execute()
    {
        plugin.getLogger().info("you should bank.");
        Time.sleepTicksUntil(() -> !Inventory.isFull(), 10);
    }
}
