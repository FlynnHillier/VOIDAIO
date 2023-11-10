package net.voids.unethicalite.cowkill.tasks;

import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Inventory;
import net.voids.unethicalite.cowkill.data.Areas;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

public class Eat extends Task
{
    public Eat(Job job)
    {
        super(job);
    }


    @Override
    public String getStatus()
    {
        return "eating food.";
    }

    @Override
    public boolean validate()
    {
        return Combat.getCurrentHealth() <= 3
                && Inventory.contains(item -> item.hasAction("Eat"));
    }

    @Override
    public void execute()
    {
        if (Players.getLocal() != null && Players.getLocal().isInteracting())
        {
            //if in combat disengage
            VoidMovement.walkTo(Areas.FALADOR_COW_FIELD.getRandomTile());
        }

        Time.sleepTick();
        Inventory.getFirst(item -> item.hasAction("Eat"))
                .interact("Eat");
        Time.sleepTicks(2);
    }
}
