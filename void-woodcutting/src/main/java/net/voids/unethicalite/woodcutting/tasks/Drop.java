package net.voids.unethicalite.woodcutting.tasks;

import com.google.common.collect.Lists;
import net.runelite.api.Item;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.voids.unethicalite.utils.tasks.Task;

import java.util.List;

public class Drop extends Task {

    @Override
    public String getStatus()
    {
        return "Dropping logs";
    }


    @Override
    public boolean validate()
    {
        return Inventory.isFull()
                && Inventory.contains(item -> item.getName().equals("Logs"));
    }

    @Override
    public void execute()
    {
        List<Item> logs = Inventory.getAll(item -> item.getName().equals("Logs"));

        for(Item log : logs)
        {
            log.drop();
            Time.sleep(250,400);
        }
    }
}
