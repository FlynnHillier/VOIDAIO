package net.voids.unethicalite.ernestTheChicken;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.PluginDescriptor;
import net.voids.unethicalite.utils.JobScript;
import org.pf4j.Extension;

@PluginDescriptor(
        name = "void-ernest-the-chicken",
        description = "complete the 'ernest the chicken' quest."
)
@Slf4j
@Extension
public class ErnestTheChickenPlugin extends JobScript
{
    public ErnestTheChickenPlugin()
    {
        job = new ErnestTheChicken();
    }
}
