package net.voids.unethicalite.woodcutting.tasks;

import net.unethicalite.api.items.Inventory;
import net.voids.unethicalite.utils.api.VoidBank;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import net.voids.unethicalite.woodcutting.WoodCutting;

import javax.inject.Inject;

public class TravelToBank extends Task
{
    public TravelToBank(Job job)
    {
        super(job);
    }

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
                && BankLocation.getNearest() != null
                && !VoidBank.nearestBankIsInteractable();
    }

    @Override
    public void execute()
    {
        VoidMovement.walkToNearestBank();
    }
}
