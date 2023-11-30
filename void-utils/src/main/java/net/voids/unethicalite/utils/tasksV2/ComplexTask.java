package net.voids.unethicalite.utils.tasksV2;

import java.util.Optional;

//TODO:
// - better improve fail handling.


/**
 * A task which will itself contain a set of sub-tasks, which it will work through.
 */
public abstract class ComplexTask extends Task
{
    private Task activeSubTask;

    private boolean completed = false;

    private int sleepOnActiveSubTaskCompletion = 0;


    public ComplexTask(String descriptor)
    {
        super(descriptor);
    }


    //ensure activeSubTask is not null
    @Override
    protected int execute()
    {
        if (activeSubTask == null)
        {
            // this should not happen
            getLogger().error("active-subtask is null");
            return -1000;
        }


        if (activeSubTask.isCompleted())
        {
            //We should set some new activeSubTask
            activeSubTask.halt();

            if (sleepOnActiveSubTaskCompletion != 0)
            {
                int sleep = sleepOnActiveSubTaskCompletion;
                sleepOnActiveSubTaskCompletion = 0;
                return sleep;
            }

            Optional<Task> nextSubTask = activeSubTask.config().onCompleteGetNextTask();

            if (nextSubTask.isPresent())
            {
                Task next = nextSubTask.get();


                if (next instanceof Complete)
                {
                    //we should complete complex task
                    completed = true;
                    return 0;
                }

                setActiveSubTask(next);
                sleepOnActiveSubTaskCompletion = next.config().sleepOnComplete;
            }
            else
            {
                if (activeSubTask.config().loopUntilSomeNextTaskShouldRun)
                {
                    return 0;
                }

                failed(Failure.Type.NO_NEXT_TASK, "could not validate any follow-up task for sub-task: " + activeSubTask.getDescriptor(), null);
                return 0;
            }
        }

        if (activeSubTask.isFailed())
        {
            getLogger().info("failed subtask: " + activeSubTask.getDescriptor());
            //Handle task failure if possible.
            Failure failure = activeSubTask.getFailure();

            if (failure.getMessage() != null)
            {
                //log failure message if present
                getLogger().info("> " + failure.getMessage());
            }

            Optional<Task> solutionTask = activeSubTask.config().onFailGetNextTask(failure.getType());

            if (solutionTask.isEmpty())
            {
                //No solution was provided. so we don't know what to do here.
                //Hence fail.

                //TODO:
                // - Instead of just passing on failed task here, perhaps wrap so handling task
                // - can distinguish that some sub-task failed.
                failed(failure.getType(), failure.getMessage(), failure.getResolveToTask());
                return 0;
            }

            //update activeSubTask to solution task.
            activeSubTask = solutionTask.get();
        }

        return activeSubTask.loop();
    }




    /**
     *
     * @param subTask the subTask to set as the active sub-task.
     */
    private void setActiveSubTask(Task subTask)
    {
        if (activeSubTask != null && !subTask.equals(activeSubTask))
        {
            //stop the previous sub-task.
            activeSubTask.halt();
        }

        this.activeSubTask = subTask;
    }


    @Override
    public boolean initialise()
    {
        if (super.initialise())
        {
            if (isFailed())
            {
                //TODO: handle when fail on initialise;
                return false;
            }

            activeSubTask = initialiseSubTasks();
            return true;
        }

        return false;
    }

    @Override
    protected void onHalt()
    {
        super.onHalt();
        if (activeSubTask != null)
        {
            activeSubTask.halt();
            activeSubTask = null;
        }
    }


    /**
     * used to construct new SubTask classes, and specify their necessary relations
     *
     * @return the SubTask that should start
     */
    protected abstract Task initialiseSubTasks();


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

    @Override
    protected void onInitialise()
    {

    }

    @Override
    public boolean completionCondition()
    {
        return completed;
    }
}
