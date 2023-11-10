package net.voids.unethicalite.cowkill.tasks;

import net.runelite.api.Item;
import net.runelite.api.TileObject;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;
import net.voids.unethicalite.cowkill.data.PluginActivity;
import net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.api.VoidBank;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Deposit extends Task
{
    public Deposit(Job job)
    {
        super(job);
    }

    @Override
    public Activity getActivity()
    {
        return Activity.BANKING;
    }

    @Override
    public String getStatus()
    {
        return "deposit bank";
    }

    @Override
    public boolean validate()
    {
        return Inventory.isFull()
                || (
                        (
                                job.isCurrentActivity(Activity.WITHDRAWING)
                                        && !job.wasPreviousActivity(Activity.BANKING)
                                )
                                || (
                                        job.isCurrentActivity(PluginActivity.COOKING)
                                            && Inventory.getCount("Cooked meat") > 4
                                )
                );
    }

    @Override
    public void execute()
    {
        if (!VoidBank.nearestBankIsInteractable())
        {
            if (!VoidMovement.walkToNearestBank())
            {
                job.getLogger().info("timed out travelling to bank.");
                return;
            }
        }

        Optional<TileObject> bankTile = VoidBank.getNearestBankTile();

        if (bankTile.isEmpty())
        {
            job.getLogger().info("unable to locate nearest banktile");
            return;
        }

        if (!Reachable.isInteractable(bankTile.get()))
        {
            job.getLogger().info("nearest bank tile not reachable.");
            return;
        }

        if (!Bank.isOpen())
        {
            if (!VoidBank.openBankAtTile(bankTile.get(), 60))
            {
                job.getLogger().info("failed to open bank tile");
                return;
            }
        }

        List<Item> toKeep =
                Bank.Inventory.getAll(item -> item.hasAction("Eat"))
                        .stream().limit(4)
                        .collect(Collectors.toList());


        VoidBank.depositAllExcept(toKeep::contains);
    }
}
