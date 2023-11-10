package net.voids.unethicalite.woodcutting.tasks;

import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import net.voids.unethicalite.utils.api.VoidBank;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;
import net.voids.unethicalite.woodcutting.WoodCutting;

import javax.inject.Inject;
import java.util.Optional;

public class Bank extends Task
{
    public Bank(Job job)
    {
        super(job);
    }

    @Inject
    WoodCutting plugin;

    @Override
    public String getStatus()
    {
        return "banking.";
    }

    @Override
    public boolean validate()
    {
        return Inventory.isFull()
                && BankLocation.getNearest() != null
                && VoidBank.nearestBankIsInteractable();
    }

    @Override
    public void execute()
    {
        Optional<TileObject> bankTile = VoidBank.getNearestBankTile();

        if (bankTile.isEmpty())
        {
            job.getLogger().info("unable to locate bank to open.");
            return;
        }


        VoidBank.openBankAtTile(bankTile.get());

        VoidBank.depositAllExcept(item -> item.getName().endsWith(" axe"));

        Time.sleepTick();
        VoidBank.close();
    }
}
