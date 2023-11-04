package net.voids.unethicalite.utils;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
//import net.unethicalite.client.Static;
import net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.tasks.Task;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//TODO: create a list of tasks, select a task if no task is present.
//Add something that can detect if task has possibly failed, this should be checked each tick


@Slf4j
public class TaskScript extends Plugin
{

    protected final List<Task> tasks = new ArrayList<>();
    Activity currentActivity = Activity.IDLE;
    Activity previousActivity;

    private ExecutorService executor;

    private Future<?> activeTask;



    @Subscribe
    private void onGameTick(GameTick event)
    {
        if (activeTask == null || activeTask.isDone())
        {
            activeTask = executor.submit(this::tick);
        }
    }


    public void tick()
    {
        for (Task t: tasks)
        {
            if (t.validate())
            {
                log.info(t.getStatus());
                t.execute();
                break;
            }
        }
    }


    public final boolean isCurrentActivity(Activity activity)
    {
        return currentActivity == activity;
    }

    public final boolean wasPreviousActivity(Activity activity)
    {
        return previousActivity == activity;
    }

    public void setActivity(Activity activity)
    {
        if (activity == Activity.IDLE && currentActivity != Activity.IDLE)
        {
            previousActivity = currentActivity;
        }

        currentActivity = activity;

        if( activity != Activity.IDLE)
        {
            //set last action tick?
        }
    }


    protected final <T extends Task> void addTask(Class<T> type)
    {
        addTask(injector.getInstance(type));
    }

    protected final void addTask(Task task)
    {
        //Static.getEventBus().register(task); //Is this necessary? - apparently event-bus allows class / method to subscribe to events
        log.info("adding task: ", task.getStatus());

        tasks.add(task);
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

        currentActivity = Activity.IDLE;

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
