package net.voids.unethicalite.woodcutting;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.voids.unethicalite.utils.JobScript;
import net.voids.unethicalite.utils.Utils;
import org.pf4j.Extension;
import javax.inject.Inject;
import net.voids.unethicalite.woodcutting.WoodCutting;


@PluginDescriptor(
        name = "void-woodcutting",
        description = "getting wood.",
        enabledByDefault = true
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class WoodCuttingPlugin extends JobScript
{
    public WoodCuttingPlugin()
    {
        job = new WoodCutting();
    }
}
