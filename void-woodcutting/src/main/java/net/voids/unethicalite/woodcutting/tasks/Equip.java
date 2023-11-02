package net.voids.unethicalite.woodcutting.tasks;

import javax.inject.Inject;

import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
//import net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.tasks.Task;
import net.voids.unethicalite.woodcutting.WoodCutting;

import java.util.Optional;

public class Equip extends Task
{
    @Inject
    private WoodCutting plugin;

    @Override
    public String getStatus()
    {
        return "Equipping items";
    }

    @Override
    public void execute()
    {
        plugin.getLogger().info("getting inv axe");
        Optional<Item> axe = getInventoryAxe();
        plugin.getLogger().info("got inv axe");

        if (axe.isPresent())
        {
            plugin.getLogger().info("axe is present.");
            Time.sleepTicks(5);
            axe.get().interact("Wield");
        }
        else
        {
            plugin.getLogger().info("axe is not present.");
        }
    }

    private static Optional<Item> getInventoryAxe()
    {
        return Optional.ofNullable(
                Inventory.getFirst(item -> item.getName().endsWith(" axe"))
        );
    }

    @Override
    public boolean validate()
    {
        return !(Equipment.fromSlot(EquipmentInventorySlot.WEAPON) != null
                && Equipment.fromSlot(EquipmentInventorySlot.WEAPON).getName().endsWith(" axe")
        )
                && Inventory.contains(item -> item.getName().endsWith("axe"));
    }
}
