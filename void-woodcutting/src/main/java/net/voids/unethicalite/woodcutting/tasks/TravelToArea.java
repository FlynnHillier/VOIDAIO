package net.voids.unethicalite.woodcutting.tasks;

import net.runelite.api.coords.WorldArea;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;
import net.voids.unethicalite.woodcutting.WoodCutting;
import net.voids.unethicalite.utils.api.VoidMovement;


import javax.inject.Inject;

public class TravelToArea extends Task
{
    public TravelToArea(Job job)
    {
        super(job);
    }

    private WorldArea TREE_PATCH_LUMBRIDGE = new WorldArea(3180, 3208, 19, 21, 0);

    @Inject
    private WoodCutting plugin;

    @Override
    public String getStatus()
    {
        return "travelling to chopping area.";
    }

    @Override
    public boolean validate()
    {
        return !Inventory.isFull()
                && !TREE_PATCH_LUMBRIDGE.contains(Players.getLocal().getWorldLocation());
    }

    @Override
    public void execute()
    {
        VoidMovement.walkToArea(TREE_PATCH_LUMBRIDGE, 100);
    }
}
