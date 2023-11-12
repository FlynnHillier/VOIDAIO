package net.voids.unethicalite.ernestTheChicken.tasks;

import net.runelite.api.Item;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.client.Static;
import net.voids.unethicalite.ernestTheChicken.data.Areas;
import net.voids.unethicalite.utils.api.VoidDialogue;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

public class UsePoisonedFishFoodOnFountain extends Task
{
    public UsePoisonedFishFoodOnFountain(Job job)
    {
        super(job);
    }

    @Override
    public String getStatus()
    {
        return "using poisoned fish food on fountain";
    }

    @Override
    public boolean validate()
    {
        return Static.getClient().getVarpValue(32) == 2
                && Inventory.contains("Poisoned fish food")
                && !Inventory.contains("Pressure gauge");
    }

    @Override
    public void execute()
    {
        if (!Areas.DRAYNOR_MANOR_FOUNTAIN.contains(Players.getLocal().getWorldLocation()))
        {
            if (!VoidMovement.walkToArea(Areas.DRAYNOR_MANOR_FOUNTAIN, 300))
            {
                job.getLogger().info("timed-out walking to fountain.");
                return;
            }
        }

        Item poisonedFishFood = Inventory.getFirst("Poisoned fish food");
        TileObject fountain = TileObjects.getNearest("Fountain");


        poisonedFishFood.useOn(fountain);

        Time.sleepTicks(8); //TODO: move search into seperate task

        fountain.interact("Search");

        if (!Time.sleepTicksUntil(Dialog::isOpen, 10))
        {
            job.getLogger().info("dialog did not appear after searching fountain");
            return;
        }

        VoidDialogue.completeDialogue();
    }
}
