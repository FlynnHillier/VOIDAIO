package net.voids.unethicalite.woodcutting.tasks;

import net.runelite.api.coords.WorldArea;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.client.Static;
import net.voids.unethicalite.utils.tasks.Task;
import net.voids.unethicalite.woodcutting.WoodCutting;

import javax.inject.Inject;

public class TravelToArea extends Task
{
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
        Movement.walkTo(TREE_PATCH_LUMBRIDGE);

        Time.sleepTicksUntil(() -> TREE_PATCH_LUMBRIDGE.contains(Players.getLocal().getWorldLocation()), 30);
    }
}
