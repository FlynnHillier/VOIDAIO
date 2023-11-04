package net.voids.unethicalite.utils;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.jobs.Job;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class JobScript extends Plugin {
    protected Job job;

    private Future<?> activeTask;
    private ExecutorService executor;

    @Subscribe
    private void onGameTick(GameTick event)
    {
        if (activeTask == null || activeTask.isDone())
        {
            activeTask = executor.submit(job::tick);
        }
    }


    protected void onStart()
    {

    }


    protected void onStop()
    {

    }


    public final void start()
    {
        log.info("Starting script: " + this.getName());

//        currentActivity = Activity.IDLE;

        onStart();
    }

    public final void stop()
    {
        log.info("Stopping script: " + this.getName());

        onStop();
    }


    @Override
    protected void shutDown()
    {
        stop();
        executor.shutdownNow();
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
