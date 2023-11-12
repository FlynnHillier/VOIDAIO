package net.voids.unethicalite.ernestTheChicken.tasks;

import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;
import net.voids.unethicalite.ernestTheChicken.data.Areas;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

public class DigCompost extends Task
{
    public DigCompost(Job job)
    {
        super(job);
    }

    @Override
    public String getStatus()
    {
        return "digging compost.";
    }

    @Override
    public boolean validate()
    {
        return Static.getClient().getVarpValue(32) == 2
                && !Inventory.contains("Key") //change this to id maybe
                && Inventory.contains("Spade");
    }

    @Override
    public void execute()
    {
        if (!Players.getLocal().getWorldLocation().equals(Areas.DRAYNOR_MANOR_COMPOST_HEAP_DIG_TILE))
        {
            VoidMovement.walkTo(Areas.DRAYNOR_MANOR_COMPOST_HEAP_DIG_TILE, 0, 300, 0);
        }

        TileObject compostHeap = TileObjects.getNearest("Compost heap");

        if (compostHeap == null)
        {
            job.getLogger().info("failed to locate compost heap");
            return;
        }

        Inventory.getFirst("Spade").useOn(compostHeap);

        boolean didFetchKey = Time.sleepTicksUntil(() ->
                Inventory.contains("Key"),
                10
        );

        if (!didFetchKey)
        {
            job.getLogger().info("failed to fetch key from compost heap.");
        }
    }
}
