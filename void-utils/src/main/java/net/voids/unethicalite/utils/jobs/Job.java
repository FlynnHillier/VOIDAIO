package net.voids.unethicalite.utils.jobs;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.unethicalite.client.Static;
import net.voids.unethicalite.utils.api.SkillMap;
import net.voids.unethicalite.utils.events.TaskChangeEvent;
import net.voids.unethicalite.utils.tasks.Task;

import java.util.ArrayList;
import java.util.List;
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
    public Task currentTask;


    private void setCurrentTask(Task task)
    {
        currentTask = task;
        Static.getEventBus().post(new TaskChangeEvent(task));
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

    public void tick()
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


//    protected final <T extends Task> void addTask(Class<T> type)
//    {
//        addTask(injector.getInstance(type));
//    }

    protected final void addTask(Task task)
    {
        //Static.getEventBus().register(task); //Is this necessary? - apparently event-bus allows class / method to subscribe to events
        tasks.add(task);
    }

    public Logger getLogger()
    {
        return log;
    }
}
