package net.voids.unethicalite.cowkill;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.voids.unethicalite.utils.JobScript;
import net.voids.unethicalite.utils.Utils;
import org.pf4j.Extension;

@PluginDescriptor(
        name = "void-cowkiller",
        description = "shanking cows.",
        enabledByDefault = true
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class CowKillerPlugin extends JobScript
{
    public CowKillerPlugin()
    {
        job = new CowKiller();
    }
}
