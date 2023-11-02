package net.voids.unethicalite.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Skill;
import net.runelite.api.events.*;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.input.Keyboard;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;
import net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.tasks.Task;
import org.slf4j.Logger;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//TODO: maybe: make scheduled task 'next' optional. This would allow 'current' to cease prematurely and suitable task to be evaluated next, without being interrupted by queue.

@Slf4j
public abstract class TickScript extends Plugin
{

    protected final List<Task> tasks = new ArrayList<>();

    private Activity currentActivity;
    private Activity previousActivity;
    private ScheduledFuture<?> current;
    private ScheduledFuture<?> next;
    protected ScheduledExecutorService executor;

    @Getter
    private boolean running;

    protected boolean idleCheckInventoryChange = false;
    protected final Map<Skill, Activity> idleCheckSkills = new HashMap<>();

    protected int lastActionTimeout = 5; //How many ticks to wait before it should be considered that the script has timed-out.
    protected int lastLoginTick = 0; //tick in which last login even occured.
    protected int lastActionTick = 0; //tick where last action occured.
    protected int lastExperienceTick = 0; //tick in which last experience point was earned
    protected int lastInventoryChangeTick = 0; //tick in which last inventory change occured. (if idleCheckInventoryChange == true)

    @Subscribe
    private void onGameTick(GameTick event)
    {
        log.info("game tick!");
        if (!running)
        {
            log.info("script disabled - return.");
            //script is not running ignore tick.
            return;
        }
        try
        {

            if (current == null)
            {
                log.info("1");
                //no current task - attempt to load one, this should only really happen on plugin boot.
                current = schedule(this::tick);
            }
            else
            {
                current.get(); //blocking.

                if (current.isDone())
                {
                    log.info("current is done.");
                    current = null;
                }
            }
//
//                //current task has ceased execution (completed).
//                if (next == null)
//                {
//                    log.info("2");
//                    //no queued task available - attempt to load one.
//                    current = schedule(this::tick);
//                }
//                else
//                {
//                    log.info("3");
//                    //a queued task exists - set it as the current task.
//                    current = next;
//                    next = null;
//                }
//            }
//            else
//            {
//                log.info("4");
//                //current task is not yet done
//                if (next == null)
//                {
//                    //attempt to queue some task.
//                    next = schedule(this::tick);
//                }
//            }

            checkActionTimeout();
            checkIdleLogout();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void tick()
    {
        log.info("calling tick");
        //select one task - and execute if predicate is valid. Tasks that occur earlier in the list, have priority over those later.
        //e.g a task in index 0 and a task in index 1 may both have valid predicates, such that t.validate() == true , however, only the task in index 0 will execute.
        for (Task t : tasks)
        {
            log.info("itter");
            log.info(String.valueOf(t.validate()));
            log.info("pre-ifs");
            if (t.validate())
            {
                log.info("was valid.");
                //task predicate is valid - so execute.
                getLogger().info(t.getStatus());
                setActivity(t.getActivity());
                log.info("executing.");
                t.execute();
                log.info("executed.");
                break;
            }
            else
            {
                log.info("invalid validation");
            }
        }
        log.info("post-for-loop tick");
    }

    protected final void addTask(Task task)
    {
        log.info("adding task");
        Static.getEventBus().register(task);

        tasks.add(task);
    }

    protected final <T extends Task> void addTask(Class<T> type)
    {
        addTask(injector.getInstance(type));
    }


    public void setActivity(Activity activity)
    {
        if (activity == Activity.IDLE && currentActivity != Activity.IDLE)
        {
            previousActivity = currentActivity;
            log.debug("Setting previous activity: " + previousActivity);
        }

        currentActivity = activity;
        log.debug("Setting current activity: " + currentActivity);

        if (activity != Activity.IDLE)
        {
            lastActionTick = Static.getClient().getTickCount();
        }
    }

    public Logger getLogger()
    {
        return log;
    }

    public final boolean isCurrentActivity(Activity activity)
    {
        return currentActivity == activity;
    }

    public final boolean wasPreviousActivity(Activity activity)
    {
        return previousActivity == activity;
    }

    protected void onStop()
    {
        log.info("default on stop");
    }

    protected void onStart()
    {
        log.info("default on start");
    }

    public final void stop()
    {
        log.info("Stopping tickscript: " + this.getName());
        running = false;

        for (Task task : tasks)
        {
            Static.getEventBus().unregister(task);
        }

        tasks.clear();

        previousActivity = Activity.IDLE;
        currentActivity = Activity.IDLE;

        onStop();
    }


    public final void start()
    {
        log.info("Starting tickscript: " + this.getName());
        running = true;

        previousActivity = Activity.IDLE;
        currentActivity = Activity.IDLE;

        onStart();
    }


    @Override
    protected final void startUp()
    {
        start();
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    protected final void shutDown()
    {
        stop();
        executor.shutdownNow();
    }

    protected void checkActionTimeout()
    {
        //check if current action is not yielding any results (probably stuck). reset Activity to idle if so.
        if (currentActivity == Activity.IDLE)
        {
            //Activity is idle - so we don't care if nothing is happening.
            return;
        }

        final int currentTick = Static.getClient().getTickCount();

        if (currentTick - lastExperienceTick < lastActionTimeout
                || currentTick - lastInventoryChangeTick < lastActionTimeout)
        {
            //not enough time has elapsed since last experience tick or last inventory change.
            return;
        }

        if (!Players.getLocal().isIdle())
        {
            //update last action tick.
            lastActionTick = currentTick;
            return;
        }

        if (currentTick - lastActionTick >= lastActionTimeout)
        {
            //last action occured x ticks ago, where x > lastActionTimeout
            setActivity(Activity.IDLE);
        }
    }

    private void checkIdleLogout()
    {
        //prevent logout due to inactivity.
        int idleClientTicks = Static.getClient().getKeyboardIdleTicks();

        if (Static.getClient().getMouseIdleTicks() < idleClientTicks)
        {
            idleClientTicks = Static.getClient().getMouseIdleTicks();
        }

        if (idleClientTicks > 12500)
        {
            log.debug("Resetting idle");

            Keyboard.type((char) KeyEvent.VK_BACK_SPACE);

            Static.getClient().setKeyboardIdleTicks(0);
            Static.getClient().setMouseIdleTicks(0);
        }
    }


    @Subscribe
    private void onWidgetHiddenChanged(WidgetHiddenChanged event)
    {
        if (Widgets.isVisible(Widgets.get(WidgetInfo.LEVEL_UP_LEVEL)))
        {
            //Handle level-up widget pop-up
            Dialog.continueSpace();
            Dialog.continueSpace();
            setActivity(Activity.IDLE);
        }
    }

    @Subscribe
    private void onStatChanged(StatChanged event)
    {
        if (!isRunning() || !Utils.isLoggedIn())
        {
            return;
        }

        for (Skill skill : idleCheckSkills.keySet())
        {
            if (event.getSkill() == skill)
            {
                setActivity(idleCheckSkills.get(skill)); //Not entirely sure why this line?
                lastExperienceTick = Static.getClient().getTickCount();
            }
        }
    }

    @Subscribe
    private void onItemContainerChanged(ItemContainerChanged event)
    {
        //Check if inventory has changed, and record tick if so.
        if (!isRunning() || !Utils.isLoggedIn())
        {
            return;
        }

        if (event.getItemContainer() != Static.getClient().getItemContainer(InventoryID.INVENTORY))
        {
            return;
        }

        if (idleCheckInventoryChange)
        {
            lastInventoryChangeTick = Static.getClient().getTickCount();
        }
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged event)
    {
        //check if login has occured, and record tick.
        if (event.getGameState() == GameState.LOGGED_IN)
        {
            lastLoginTick = Static.getClient().getTickCount();
        }
    }

    protected ScheduledFuture<?> schedule(Runnable runnable)
    {
        //push a new runnable (task) to the executor stack.

        return executor.schedule(
                runnable,
                5000, //change this? not sure what is good amount.
                TimeUnit.MILLISECONDS
        );
    }
}