package net.voids.unethicalite.voidaio;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.plugins.Script;
import org.pf4j.Extension;


@Extension
@PluginDescriptor(
		name = "VOID-AIO",
		description = "Does the fokkin lot.",
		enabledByDefault = true
)

@Slf4j
public class VoidAIO extends Script
{

	public int loop()
	{
		return 1000;
	}

	@Override
	public void onLogin()
	{
		super.onLogin();
	}

	@Override
	public void onStart(String... strings)
	{
		log.info("Started: ", this.getName());
	}
}
