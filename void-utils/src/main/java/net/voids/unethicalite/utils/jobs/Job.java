package net.voids.unethicalite.utils.jobs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.voids.unethicalite.utils.api.SkillMap;
import net.voids.unethicalite.utils.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import com.google.inject.Injector;
import org.slf4j.Logger;


@Slf4j
public abstract class Job {

    protected List<Task> tasks = new ArrayList<>();

    @Getter
    protected SkillMap skillRequirements;

    @Getter
    protected SkillMap skillRecommendations;

    @Getter
    @Setter
    public Task currentTask;


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
