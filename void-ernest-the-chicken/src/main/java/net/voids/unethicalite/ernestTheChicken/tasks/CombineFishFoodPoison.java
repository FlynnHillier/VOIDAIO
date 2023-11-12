package net.voids.unethicalite.ernestTheChicken.tasks;

import net.runelite.api.Item;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

public class CombineFishFoodPoison extends Task
{
    public CombineFishFoodPoison(Job job)
    {
        super(job);
    }

    @Override
    public String getStatus()
    {
        return "combining fish food and poison.";
    }

    @Override
    public boolean validate()
    {
        return Static.getClient().getVarpValue(32) <= 2
                && Inventory.contains("Poison")
                && Inventory.contains("Fish food");
    }

    @Override
    public void execute()
    {
        Item poison = Inventory.getFirst("Poison");
        Item fishFood = Inventory.getFirst("Fish food");

        poison.useOn(fishFood);

        Time.sleepTicksUntil(() -> Inventory.contains("Poisoned fish food"), 10);
    }
}
