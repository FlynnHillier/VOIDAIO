package net.voids.unethicalite.cowkill.tasks;

import net.runelite.api.Item;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Production;
import net.voids.unethicalite.cowkill.data.Areas;
import net.voids.unethicalite.cowkill.data.PluginActivity;
import net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

public class CookBeef extends Task
{
    public CookBeef(Job job)
    {
        super(job);
    }

    @Override
    public Activity getActivity()
    {
        return PluginActivity.COOKING;
    }

    @Override
    public String getStatus()
    {
        return "Cooking beef";
    }

    @Override
    public boolean validate()
    {
        return Inventory.contains("Raw beef")
                && job.isCurrentActivity(Activity.WITHDRAWING);
    }

    @Override
    public void execute()
    {
        if (Players.getLocal().getWorldLocation() != Areas.FALADOR_COOKER_TILE)
        {
            VoidMovement.walkTo(Areas.FALADOR_COOKER_TILE, 0, 120, 0);
        }

        TileObject cooker = TileObjects.getNearest(tile -> tile.getName().equals("Range"));

        if (!cooker.hasAction("Cook"))
        {
            job.getLogger().info("no option to cook on range");
            return;
        }

        cooker.interact("Cook");

        if (!Time.sleepTicksUntil(Production::isOpen, 10))
        {
            job.getLogger().info("timed out waiting for cooking production menu to open.");
            return;
        }

        if (Inventory.contains("Raw beef"))
        {
            Production.chooseOption("Raw beef");
            Time.sleepTicksUntil(() -> !Production.isOpen(), 4);
            if (Production.isOpen())
            {
                Production.chooseOption("Cooked meat");
            }
        }

        if (!Time.sleepTicksUntil(() -> !Production.isOpen(), 4))
        {
            job.getLogger().info("error choosing cooking production option");
            return;
        }

        Time.sleepTicksUntil(() ->
             !Inventory.contains("Raw beef")
        , 50);

        if (Inventory.contains("Burnt meat"))
        {
            for (Item burntMeat : Inventory.getAll("Burnt meat"))
            {
                burntMeat.drop();
                Time.sleep(500, 600);
            }
        }


    }
}
