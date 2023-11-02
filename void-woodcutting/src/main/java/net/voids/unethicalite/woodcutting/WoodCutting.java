package net.voids.unethicalite.woodcutting;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
//import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.voids.unethicalite.utils.TaskScript;
//import net.voids.unethicalite.utils.TickScript;
//import net.voids.unethicalite.utils.Utils;
import net.voids.unethicalite.utils.Utils;
import net.voids.unethicalite.woodcutting.tasks.*;
import org.pf4j.Extension;
//import org.slf4j.Logger;

import javax.inject.Inject;

@PluginDescriptor(
        name = "void-woodcutting",
        description = "getting wood.",
        enabledByDefault = true
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class WoodCutting extends TaskScript
{
    @Inject
    private Utils utils;

    @Override
    protected void onStart()
    {
        getLogger().info("starting void-woodcutter.");
        super.onStart();

        addTask(Equip.class);
        addTask(Drop.class);
        addTask(TravelToArea.class);
        addTask(Chop.class);
    }
}
