package net.voids.unethicalite.ernestTheChicken.tasks;

import net.runelite.api.TileItem;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.client.Static;
import net.voids.unethicalite.ernestTheChicken.data.Areas;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

import java.util.Optional;

public class CollectSpade extends Task
{
    public CollectSpade(Job job)
    {
        super(job);
    }


    @Override
    public String getStatus()
    {
        return "collecting spade.";
    }

    @Override
    public boolean validate()
    {
        return Static.getClient().getVarpValue(32) == 2
                && !Inventory.contains("Spade")
                && Inventory.contains("Oil can")
                && !Inventory.contains("Pressure gauge");
    }

    @Override
    public void execute()
    {
        if (!Areas.DRAYNOR_MANOR_COURT_YARD_AND_GROUND_FLOOR.contains(Players.getLocal()))
        {
            VoidMovement.walkToPolygonArea(Areas.DRAYNOR_MANOR_COURT_YARD_AND_GROUND_FLOOR, 300);
        }


        Optional<TileItem> spade;


        do
        {
            spade = Optional.ofNullable(
                    TileItems.getNearest(item ->
                            item.getName().equals("Spade")
                                    && Areas.DRAYNOR_MANOR_COURT_YARD_AND_GROUND_FLOOR.contains(item.getWorldLocation())
                    )
            );

            if (spade.isEmpty())
            {
                if (!Players.getLocal().getWorldLocation().equals(Areas.DRAYNOR_MANOR_SPADE_SPAWN_TILE))
                {
                    VoidMovement.walkTo(Areas.DRAYNOR_MANOR_SPADE_SPAWN_TILE, 0, 300, 0);
                }
                Time.sleepTick();
            }

            if (!job.isRunning())
            {
                return;
            }
        } while (spade.isEmpty());

        if (!Reachable.isWalkable(spade.get().getWorldLocation()) || spade.get().getWorldLocation().distanceTo(Players.getLocal().getWorldLocation()) > 5)
        {
            VoidMovement.walkTo(spade.get().getWorldLocation(), 0, 300, 0);
        }

        spade.get().interact("Take");

        Optional<TileItem> finalSpade = spade;
        boolean spadeWasCollected = Time.sleepTicksUntil(() ->
                !finalSpade.get().getTile().getGroundItems().contains(finalSpade.get())
        , 30);

        if (!spadeWasCollected)
        {
            job.getLogger().info("Timed out attempting to collect spade after interaction");
        }
    }
}
