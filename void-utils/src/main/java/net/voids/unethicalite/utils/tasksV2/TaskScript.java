package net.voids.unethicalite.utils.tasksV2;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.unethicalite.api.plugins.LoopedPlugin;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public abstract class TaskScript extends LoopedPlugin
{
    public TaskScript(Task task)
    {
        this.task = task;
    }

    protected Task task;

    @Setter
    private boolean running = false;

    private String lastDescriptor = "";

    ExecutorService executor;


    @Override
    protected int loop()
    {
        while (running)
        {
            if (task.isFailed())
            {
                log.info(task.getFailure().getMessage());
                stop();
                return 0;
            }
            else if (task.isCompleted())
            {
                log.info("completed task.");
                stop();
                return 0;
            }

            int sleep = task.loop();
            if (!task.getDescriptor().equals(lastDescriptor))
            {
                log.info(task.getDescriptor());
                lastDescriptor = task.getDescriptor();
            }

            return sleep;
        }
        return 1;
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
