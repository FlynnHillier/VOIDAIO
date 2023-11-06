package net.voids.unethicalite.utils.jobs;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.client.Static;
import net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.api.SkillMap;
import net.voids.unethicalite.utils.events.JobEndEvent;
import net.voids.unethicalite.utils.events.TaskChangeEvent;
import net.voids.unethicalite.utils.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;

//TODO: consider that some tasks may need to be interrupted (e.g) running through a dungeon
// - your health gets low - you want to carry on running but some task shoudl run that eats.
// - currently, unless you return from the task which is running, the 'eat' task cannot fire.
// - add some flag to 'running task' which specifies it *can* be interrupted. In this scenario
// - other tasks validates should run - and should be executed with precident.
// - (this idea would chain well with Activities)


@Slf4j
public abstract class Job
{
    protected List<Task> tasks = new ArrayList<>();

    @Getter
    protected SkillMap skillRequirements;

    @Getter
    protected SkillMap skillRecommendations;

    @Getter
    private Task currentTask;

    @Getter
    private Activity currentActivity = Activity.IDLE;

    @Getter
    private Activity previousActivity = Activity.IDLE;

    @Getter
    private boolean running = false;

    private Future<?> activeTick;

    private ExecutorService executor;




    public void start()
    {
        setRunning(true);
    }

    public void stop()
    {
        setRunning(false);
    }

    public abstract String getTitle();

    public boolean isSafeToEnd()
    {
        if (currentTask == null)
        {
            return true;
        }
        return currentTask.isSafeToEnd();
    }



    @Subscribe
    public void onTick(GameTick event)
    {
        if (!running || (activeTick != null && !activeTick.isDone() && !currentTask.isInterruptable()))
        {
            return;
        }
        activeTick = executor.submit(this::executeTask);
    }

    private void executeTask()
    {
        for (Task task: tasks)
        {
            if (task.validate())
            {
                setCurrentTask(task);
                task.execute();
                break;
            }
        }
    }






    protected boolean hasSkillRequirements()
    {
        return skillRequirements.playerHasRequirements();
    }

    protected boolean hasSkillRecommendations()
    {
        return skillRecommendations.playerHasRequirements();
    }

    protected boolean isCurrentActivity(Activity activity)
    {
        return currentActivity == activity;
    }

    protected boolean wasPreviousActivity(Activity activity)
    {
        return previousActivity == activity;
    }


    private void setRunning(boolean bool)
    {
        if (bool && !running)
        {
            //start job

            running = true;
            Static.getEventBus().register(this);
            for (Task task : tasks)
            {
                //register task classes so they can receive game events.
                Static.getEventBus().register(task);
            }

            executor = Executors.newSingleThreadExecutor();
        }
        else if (!bool && running)
        {
            //stop job

            running = false;
            setCurrentActivity(Activity.IDLE);

            Static.getEventBus().unregister(this);
            for (Task task : tasks)
            {
                //unregister task classes so they cease receiving game events.
                Static.getEventBus().unregister(task);
            }
            Static.getEventBus().post(new JobEndEvent());

            activeTick.cancel(true);
            executor.shutdownNow();
            executor = null;
        }
    }


    private void setCurrentTask(Task task)
    {
        currentTask = task;
        Static.getEventBus().post(new TaskChangeEvent(task));
    }

    public void setCurrentActivity(Activity activity)
    {
        if (activity != currentActivity)
        {
            previousActivity = currentActivity;
            currentActivity = activity;
        }

        if (activity != Activity.IDLE)
        {

        }
    }


//    protected final <T extends Task> void addTask(Class<T> type)
//    {
//        addTask(injector.getInstance(type));
//    }

    protected final void addTask(Task task)
    {
        if (running)
        {
            Static.getEventBus().register(task);
        }
        tasks.add(task);
    }

    public Logger getLogger()
    {
        return log;
    }
}
