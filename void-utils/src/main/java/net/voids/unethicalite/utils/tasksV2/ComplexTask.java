package net.voids.unethicalite.utils.tasksV2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A task which will itself contain a set of sub-tasks, which it will work through.
 */
public abstract class ComplexTask extends Task
{
    protected List<Task> subTasks = new ArrayList<>();
    private Task activeSubTask;

    protected boolean failOnNoValidate = false; //if no subTask is found to be validated, and the task is not yet complete.


    public ComplexTask(String descriptor)
    {
        super(descriptor);
    }

    @Override
    public void execute()
    {
        if (activeSubTask == null || activeSubTask.isCompleted())
        {
            //We should set some new activeSubTask
            if (activeSubTask != null && activeSubTask.isCompleted())
            {
//                if (activeSubTask.getSleepOnCompletion() != null)
//                {
//                    activeSubTask.getSleepOnCompletion().sleep();
//                }
            }

            Optional<Task> nextSubTask = getNextSubTask();

            if (nextSubTask.isEmpty())
            {
                if (failOnNoValidate)
                {
                    failed(Failure.Type.NO_VALIDATE, "could not validate any sub", null);
                }

                //we did not validate but do not wish to fail, so just return.
                //hopefully next itteration will activate some task.

                //TODO:
                // - Add functionality to check for timeout here.
                return;
            }

            setActiveSubTask(nextSubTask.get());
        }

        if (activeSubTask.isFailed())
        {
            getLogger().info("failed subtask: " + activeSubTask.getDescriptor());
            //Handle task failure if possible.
            Failure failure = activeSubTask.getFailure();

            if (failure.getMessage() != null)
            {
                //log failure message if present
                getLogger().info(failure.getMessage());
            }

            Optional<Task> solutionTask = handleFailure(failure);

            if (solutionTask.isEmpty())
            {
                //No solution was provided. so we dont know what to do here.
                //Hence fail.

                //TODO:
                // - Instead of just passing on failed task here, perhaps wrap so handling task
                // - can distinguish that some sub-task failed.
                failed(failure.getType(), failure.getMessage(), failure.getResolveToTask());
                return;
            }

            //update activeSubTask to solution task.
            activeSubTask = solutionTask.get();
        }

        activeSubTask.loop();
    }

    private Optional<Task> getNextSubTask()
    {
        for (Task subTask : subTasks)
        {
            if (subTask.isValidated())
            {
                return Optional.of(subTask);
            }
        }

        return Optional.empty();
    }


    /**
     *
     * @param task the task to set as the active sub-task.
     */
    private void setActiveSubTask(Task task)
    {
        if (activeSubTask != null && !task.equals(activeSubTask))
        {
            //stop the previous sub task.
            activeSubTask.halt();
        }


        this.activeSubTask = task;
    }


    /**
     *
     * @param task to be added to subTasks.
     */
    protected void addSubTask(Task task)
    {
        subTasks.add(task);
    }

    /**
     * clears all sub-tasks
     */
    protected void clearSubTasks()
    {
        subTasks = new ArrayList<>();
    }


    @Override
    public boolean initialise()
    {
        if (super.initialise())
        {
            clearSubTasks();
            return true;
        }

        return false;
    }


    /**
     *
     * @return a descriptor describing the current task. As well as a description of the active sub-task if present.
     */
    @Override
    public String getDescriptor()
    {
        if (activeSubTask == null)
        {
            return descriptor;
        }

        return descriptor + " : " + activeSubTask.getDescriptor();
    }
}
