package net.voids.unethicalite.ernestTheChicken.tasks;
import net.runelite.api.TileItem;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
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

public class CollectFishFood extends Task
{
    public CollectFishFood(Job job)
    {
        super(job);
    }


    @Override
    public boolean validate()
    {
        return Static.getClient().getVarpValue(32) <= 2
                && !Inventory.contains("Poisoned fish food")
                && !Inventory.contains("Fish food")
                && !Inventory.contains("Key")
                && (
                        (
                                Areas.DRAYNOR_MANOR_MIDDLE_FLOOR.contains(Players.getLocal().getWorldLocation())
                                        && TileItems.getNearest("Fish food") != null)
                                || Areas.DRAYNOR_MANOR_GROUND_FLOOR.contains(Players.getLocal().getWorldLocation())
                                || (
                                        Static.getClient().getVarpValue(32) == 2
                                                && (
                                                    !Areas.DRAYNOR_MANOR_MIDDLE_FLOOR.contains(Players.getLocal().getWorldLocation())
                                                            || Inventory.contains("Poison")
                                                )
                                )
                );
    }

    @Override
    public String getStatus()
    {
        return "fetching fish food";
    }

    @Override
    public void execute()
    {
        if (!Areas.DRAYNOR_MANOR_MIDDLE_FLOOR.contains(Players.getLocal().getWorldLocation()))
        {
            //landing of staircases within middle floor
            VoidMovement.walkToArea(new WorldArea(3104, 3362, 10, 7, 1), 50);
        }


        //fetch the fish food.
        //if the fish food is not present and we don't have poison, return.
        //if the fish food is not present and we do have poison
        TileItem tileItem_FishFood;
        do
        {
            tileItem_FishFood = TileItems.getNearest(
                    item -> item.getName().equals("Fish food")
            );

            if (tileItem_FishFood == null)
            {
                if (!Inventory.contains("Poison") || Static.getClient().getVarpValue(32) != 2)
                {
                    job.getLogger().info("fish food not spawned.");
                    Time.sleepTicks(2);
                    return;
                }

                WorldPoint fishFoodSpawnTile = new WorldPoint(3108, 3356, 1);

                if (!Players.getLocal().getWorldLocation().equals(fishFoodSpawnTile))
                {
                    VoidMovement.walkTo(fishFoodSpawnTile);
                }

                Time.sleepTick();
                if (!job.isRunning())
                {
                    return;
                }
            }
        } while (tileItem_FishFood == null);


        if (!Reachable.isWalkable(tileItem_FishFood.getWorldLocation()))
        {
            VoidMovement.walkTo(tileItem_FishFood.getWorldLocation(), 1, 50, 2);
        }

        if (tileItem_FishFood.hasAction("Take"))
        {
            tileItem_FishFood.interact("Take");
        }

        TileItem finalTileItem_FishFood = tileItem_FishFood;
        if (!Time.sleepTicksUntil(() ->
                !finalTileItem_FishFood.getTile().getGroundItems().contains(finalTileItem_FishFood), 40)
        )
        {
            job.getLogger().info("timed out waiting for fish food to be picked up after interaction");
            return;
        }
    }
}
