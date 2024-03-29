package net.voids.unethicalite.aio;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.voids.unethicalite.aio.managers.SkillManager;
import net.voids.unethicalite.utils.Utils;
import net.voids.unethicalite.utils.jobs.Job;
import org.pf4j.Extension;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@PluginDescriptor(
        name = "void-AIO",
        description = "does the fokkin lot.",
        enabledByDefault = true
)
@PluginDependency(Utils.class)
@Extension
@Slf4j
public class AIO extends Plugin
{
    SkillManager sm = new SkillManager();

    Future<?> activeJobTick;
    ExecutorService executor;

    private Job currentJob;




    @Subscribe
    private void onGameTick(GameTick event)
    {
        currentJob = sm.tick();
    }

    public final void start()
    {
        if (currentJob != null)
        {
            currentJob.start();
        }

        log.info("Starting script: " + this.getName());
//        currentActivity = Activity.IDLE;
    }

    public final void stop()
    {
        log.info("Stopping script: " + this.getName());
        currentJob.stop();
    }


    @Override
    protected void shutDown()
    {

        executor.shutdownNow();
        stop();
    }

    @Override
    protected void startUp()
    {
        executor = Executors.newSingleThreadExecutor();
        start();
    }

    public Logger getLogger()
    {
        return log;
    }
}
