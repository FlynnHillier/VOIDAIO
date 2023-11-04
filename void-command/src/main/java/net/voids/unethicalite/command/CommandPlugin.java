package net.voids.unethicalite.command;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.voids.unethicalite.utils.Utils;
import org.pf4j.Extension;

@Slf4j
@PluginDescriptor(
        name = "void-command",
        description = "they shall all fear me."
)
@PluginDependency(Utils.class)
@Extension
public class CommandPlugin extends Plugin
{
    private SocketCommunication socket = new SocketCommunication();


    @Override
    protected void shutDown()
    {
        socket.stopHeartbeat();

        log.info("stopping " + this.getName());
    }

    @Override
    protected void startUp()
    {
        socket.startHeartbeat();

        log.info("starting " + this.getName());
    }
}
