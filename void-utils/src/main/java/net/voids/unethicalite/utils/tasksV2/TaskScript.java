package net.voids.unethicalite.utils.tasksV2;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public abstract class TaskScript extends Plugin
{
    public TaskScript(Task task)
    {
        this.task = task;
    }

    protected Task task;

    @Setter
    private boolean running = false;

    ExecutorService executor;


    private void loop()
    {
        while (running)
        {
            if (!task.isFailed())
            {
                if (task.isCompleted())
                {
                    log.info("completed task.");
                    stop();
                }
                else
                {
                    log.info(task.getDescriptor());
                    task.loop();
                }
            }
            else
            {
                log.info(task.getFailure().getMessage());
                stop();
            }
        }
    }



    public final void start()
    {
        log.info("Starting script: " + this.getName());
        setRunning(true);
        executor.submit(this::loop);
    }

    public final void stop()
    {
        log.info("Stopping script: " + this.getName());
        task.halt();
        setRunning(false);
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
