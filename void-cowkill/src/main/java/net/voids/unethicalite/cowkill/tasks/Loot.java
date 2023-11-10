package net.voids.unethicalite.cowkill.tasks;

import net.runelite.api.TileItem;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;
import net.voids.unethicalite.cowkill.data.Areas;
import net.voids.unethicalite.cowkill.data.PluginActivity;
import net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

import java.util.List;

public class Loot extends Task
{
    private TileItem currentLootTarget;

    public Loot(Job job)
    {
        super(job);
    }

    @Override
    public Activity getActivity()
    {
        return PluginActivity.LOOTING;
    }

    @Override
    public String getStatus()
    {
        return "looting.";
    }

    @Override
    public boolean validate()
    {
        if (Players.getLocal() == null || Players.getLocal().isInteracting())
        {
            //player is busy
            return false;
        }


        if (currentLootTarget != null && (currentLootTarget.getTile().getGroundItems() == null || !currentLootTarget.getTile().getGroundItems().contains(currentLootTarget)))
        {
            //validate previous loot target
            currentLootTarget = null;
        }

        if (currentLootTarget == null)
        {
            //assign new loot target if necessary.
            currentLootTarget = TileItems.getNearest(item ->
                    item.hasAction("Take")
                            && Areas.FALADOR_COW_FIELD.contains(item.getWorldLocation())
                            && (
                                    item.getName().equals("Cowhide")
                                            || (
                                                    item.getName().equals("Raw beef")
                                                            && Inventory.getCount("Raw beef") < 3
                                    )
                            )
                    );
        }

        return currentLootTarget != null;
    }

    @Override
    public void execute()
    {
        if (Players.getLocal() != null && !Areas.FALADOR_COW_FIELD.contains(Players.getLocal().getWorldLocation()))
        {
            if (!VoidMovement.walkToPolygonArea(Areas.FALADOR_COW_FIELD, 100))
            {
                //timed out while travelling to cow field.
                return;
            }
        }


        if (!Reachable.isInteractable(currentLootTarget))
        {
            VoidMovement.walkTo(currentLootTarget.getWorldLocation());
        }

        currentLootTarget.interact("Take");

        // sleep until item is picked up.
        List<TileItem> tileItemSnapshot = currentLootTarget.getTile().getGroundItems();

        Time.sleepTicksUntil(() -> !currentLootTarget.getTile().getGroundItems().equals(tileItemSnapshot), 10);

        currentLootTarget = null;
    }
}
