package net.voids.unethicalite.utils;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import net.voids.unethicalite.utils.jobs.Job;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class JobScript extends Plugin
{
    protected Job job;
    private ExecutorService executor;


    protected void onStart()
    {
        job.start();
    }


    protected void onStop()
    {
        job.stop();
    }


    public final void start()
    {
        log.info("Starting script: " + this.getName());
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
