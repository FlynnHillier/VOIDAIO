package net.voids.unethicalite.ernestTheChicken.tasks;

import net.runelite.api.TileItem;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;
import net.voids.unethicalite.ernestTheChicken.data.Areas;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

import java.util.Optional;

public class CollectRubberTube extends Task
{
    public CollectRubberTube(Job job)
    {
        super(job);
    }

    @Override
    public String getStatus()
    {
        return "collecting rubber tube";
    }

    @Override
    public boolean validate()
    {
        return Static.getClient().getVarpValue(32) == 2
                && Combat.getCurrentHealth() > 5
                && !Inventory.contains("Rubber tube")
                && Inventory.contains("Key");
    }

    @Override
    public void execute()
    {
        WorldPoint nextToDoor = new WorldPoint(3111, 3377, 0);


        if (!Players.getLocal().getWorldLocation().equals(nextToDoor))
        {
            VoidMovement.walkTo(nextToDoor, 0, 300, 0);
        }

        Optional<TileItem> rubberTube = Optional.ofNullable(
                TileItems.getNearest(tileItem ->
                        tileItem.getName().equals("Rubber tube")
                                && Areas.DRAYNOR_MANOR_GROUND_FLOOR.contains(tileItem)
                )
        );

        if (rubberTube.isEmpty())
        {
            job.getLogger().info("failed to locate rubber tube");
            return;
        }

        VoidMovement.walkTo(rubberTube.get().getWorldLocation(), 0, 50, 0);

        rubberTube.get().interact("Take");

        Time.sleepTicksUntil(() -> !rubberTube.get().getTile().getGroundItems().contains(rubberTube.get()), 10);
        VoidMovement.walkTo(nextToDoor, 0, 20, 0);
    }
}
