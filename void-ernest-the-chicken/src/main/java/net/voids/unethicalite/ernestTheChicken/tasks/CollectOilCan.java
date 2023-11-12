package net.voids.unethicalite.ernestTheChicken.tasks;

import net.runelite.api.TileItem;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

public class CollectOilCan extends Task
{
    public CollectOilCan(Job job)
    {
        super(job);
    }

    @Override
    public String getStatus()
    {
        return "collecting oil can";
    }

    @Override
    public boolean validate()
    {
        return !Inventory.contains("Oil can")
                && Static.getClient().getVarpValue(32) == 2
                && (
                    Players.getLocal().getWorldLocation().getRegionID() != 12440
                            || (
                                TileItems.getNearest("Oil can") != null
                                        && Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_9_STATE) == 1
                                        && Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_8_STATE) == 1
                                        && Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_3_STATE) == 1
                    )
                );
    }

    @Override
    public void execute()
    {
        if (Players.getLocal().getWorldLocation().getRegionID() != 12440)
        {
            VoidMovement.walkTo(new WorldPoint(3116, 9753, 0));
        }

        TileItem tileItem_oilCan = TileItems.getNearest("Oil can");

        if (!(Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_9_STATE) == 1
                && Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_8_STATE) == 1
                && Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_3_STATE) == 1))
        {
            job.getLogger().info("oil can is not yet reachable.");
            return;
        }
        else
        {
            VoidMovement.walkTo(tileItem_oilCan.getWorldLocation(), 0, 100, 0);
        }

        tileItem_oilCan.interact("Take");
    }
}
