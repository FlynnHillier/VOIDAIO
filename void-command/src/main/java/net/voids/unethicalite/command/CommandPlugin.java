package net.voids.unethicalite.command;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.voids.unethicalite.utils.Utils;
import net.voids.unethicalite.utils.events.JobEndEvent;
import net.voids.unethicalite.utils.events.TaskChangeEvent;
import org.pf4j.Extension;


@Slf4j
@PluginDescriptor(
        name = "void-command",
        description = "command them all."
)
@PluginDependency(Utils.class)
@Extension
public class CommandPlugin extends Plugin
{
    private final SocketCommunication socket = SocketCommunication.getInstance();

    private final State state = State.getInstance();


    @Subscribe
    private void onTaskChange(TaskChangeEvent event)
    {
        state.onTaskChange(event);
    }

    @Subscribe
    private void onJobEnd(JobEndEvent event)
    {
        log.info("i smell a job-end event!");
        state.onJobEnd(event);
    }


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
