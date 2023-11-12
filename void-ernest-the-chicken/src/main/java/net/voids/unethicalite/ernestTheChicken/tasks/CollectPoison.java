package net.voids.unethicalite.ernestTheChicken.tasks;
import net.runelite.api.TileItem;
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

public class CollectPoison extends Task
{
    public CollectPoison(Job job)
    {
        super(job);
    }


    @Override
    public boolean validate()
    {
        return Static.getClient().getVarpValue(32) <= 2
                && !Inventory.contains("Poisoned fish food")
                && !Inventory.contains("Poison")
                && !Inventory.contains("Key")
                && (
                        Static.getClient().getVarpValue(32) == 2
                                && (
                                        Inventory.contains("Fish food")
                                                || !Areas.DRAYNOR_MANOR_GROUND_FLOOR.contains(Players.getLocal().getWorldLocation())
                                )
                        || (
                                Players.getLocal().getPlane() == 0
                                        && !Areas.DRAYNOR_MANOR_GROUND_FLOOR.contains(Players.getLocal().getWorldLocation()))
                        || (
                                Players.getLocal().getPlane() == 0
                                        && Areas.DRAYNOR_MANOR_GROUND_FLOOR.contains(Players.getLocal().getWorldLocation())
                                        && TileItems.getNearest("Poison") != null)
                );
    }

    @Override
    public String getStatus()
    {
        return "fetching poison";
    }

    @Override
    public void execute()
    {
        if (!Areas.DRAYNOR_MANOR_GROUND_FLOOR.contains(Players.getLocal().getWorldLocation()))
        {
            //ground floor of Draynor manor
            VoidMovement.walkToPolygonArea(Areas.DRAYNOR_MANOR_GROUND_FLOOR);
        }


        TileItem tileItem_Poison;
        do
        {
            tileItem_Poison = TileItems.getNearest(item ->
                    item.getName().equals("Poison")
                            && Areas.DRAYNOR_MANOR_GROUND_FLOOR.contains(item.getWorldLocation())
            );

            if (tileItem_Poison == null)
            {
                if (!Inventory.contains("Fish food") || Static.getClient().getVarpValue(32) != 2)
                {
                    job.getLogger().info("poison not spawned.");
                    return;
                }

                WorldPoint poisonSpawnTile = new WorldPoint(3097, 3366, 0);

                if (!Players.getLocal().getWorldLocation().equals(poisonSpawnTile))
                {
                    VoidMovement.walkTo(poisonSpawnTile, 0, 40, 0);
                }

                Time.sleepTick();
                if (!job.isRunning())
                {
                    return;
                }
            }
        } while (tileItem_Poison == null);


        if (!Reachable.isWalkable(tileItem_Poison.getWorldLocation()))
        {
            VoidMovement.walkTo(tileItem_Poison.getWorldLocation(), 0, 50, 0);
        }

        if (tileItem_Poison.hasAction("Take"))
        {
            tileItem_Poison.interact("Take");
        }

        TileItem finaltileItem_Poison = tileItem_Poison;
        if (!Time.sleepTicksUntil(() ->
                !finaltileItem_Poison.getTile().getGroundItems().contains(finaltileItem_Poison), 60)
        )
        {
            job.getLogger().info("timed out waiting for poison to be picked up after interaction");
            return;
        }
    }
}
