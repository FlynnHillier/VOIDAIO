package net.voids.unethicalite.cowkill.tasks;

import net.runelite.api.TileObject;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;
import net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.api.VoidBank;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

import java.util.Optional;

public class Withdraw extends Task
{
    public Withdraw(Job job)
    {
        super(job);
    }

    @Override
    public String getStatus()
    {
        return "withdrawing food from bank.";
    }

    @Override
    public Activity getActivity()
    {
        return Activity.WITHDRAWING;
    }

    @Override
    public boolean validate()
    {
        return !Inventory.isFull()
                && (
                        (
                                Inventory.getCount(item -> item.hasAction("Eat")) == 0
                                && (
                                        (
                                                job.isCurrentActivity(Activity.ATTACKING)
                                                        && Combat.getCurrentHealth() <= 6
                                                        && !Players.getLocal().isInteracting()
                                        )
                                )
                                        || (
                                                job.isCurrentActivity(Activity.ATTACKING)
                                                && Combat.getCurrentHealth() <= 3
                                        )
                                || (
                                        job.isCurrentActivity(Activity.BANKING)
                                                && Inventory.getCount(item -> item.hasAction("Eat")) < 4
                                )
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
            if (!VoidBank.openBankAtTile(bankTile.get()))
            {
                job.getLogger().info("failed to open bank tile");
                return;
            }
        }

        while (Inventory.getCount(item -> item.hasAction("Eat")) < 4 && Bank.contains(item -> item.getId() == 2142))
        {
            //withdraw cooked meat while possible
            Bank.withdraw(
                    item -> item.getId() == 2142, //cooked meat (change this to include other foods in future)
                    4 - Inventory.getCount(item -> item.hasAction("Eat")),
                    Bank.WithdrawMode.ITEM
            );
        }

        if (Inventory.getCount(item -> item.getId() == 2142) < 4)
        {
            //bank did not contain enough cooked meat. withdraw raw beef to go cook
            Bank.withdraw(
                    item -> item.getName().equals("Raw beef"),
                    Integer.MAX_VALUE,
                    Bank.WithdrawMode.ITEM
            );
        }




    }
}
