package net.voids.unethicalite.utils.tasksV2;

import java.util.Optional;

//TODO:
// - better improve fail handling.


/**
 * A task which will itself contain a set of sub-tasks, which it will work through.
 */
public abstract class ComplexTask extends Task
{
    private SubTask activeSubTask;

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


        if (activeSubTask.getTask().isCompleted())
        {
            //We should set some new activeSubTask
            activeSubTask.getTask().halt();

            if (sleepOnActiveSubTaskCompletion != 0)
            {
                int sleep = sleepOnActiveSubTaskCompletion;
                sleepOnActiveSubTaskCompletion = 0;
                return sleep;
            }

            Optional<NextTask> nextSubTask = activeSubTask.getNextTask();

            if (nextSubTask.isPresent())
            {
                if (nextSubTask.get().getSubTask() instanceof Complete)
                {
                    //we should complete complex task
                    completed = true;
                    return 0;
                }

                setActiveSubTask(nextSubTask.get().getSubTask());
                sleepOnActiveSubTaskCompletion = nextSubTask.get().getSleepOnComplete();
            }
            else
            {
                if (activeSubTask.isLoopUntilSomeValidNext())
                {
                    return 0;
                }

                failed(Failure.Type.NO_VALIDATE, "could not validate any follow-up task for sub-task: " + activeSubTask.getTask().getDescriptor(), null);
                return 0;
            }
        }

        if (activeSubTask.getTask().isFailed())
        {
            getLogger().info("failed subtask: " + activeSubTask.getTask().getDescriptor());
            //Handle task failure if possible.
            Failure failure = activeSubTask.getTask().getFailure();

            if (failure.getMessage() != null)
            {
                //log failure message if present
                getLogger().info(failure.getMessage());
            }

            Optional<SubTask> solutionTask = activeSubTask.getHandleFailureTask(failure.getType());

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

        return activeSubTask.getTask().loop();
    }




    /**
     *
     * @param subTask the subTask to set as the active sub-task.
     */
    private void setActiveSubTask(SubTask subTask)
    {
        if (activeSubTask != null && !subTask.equals(activeSubTask))
        {
            //stop the previous sub-task.
            activeSubTask.getTask().halt();
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
            activeSubTask.getTask().halt();
            activeSubTask = null;
        }
    }


    /**
     * used to construct new SubTask classes, and specify their necessary relations
     *
     * @return the SubTask that should start
     */
    protected abstract SubTask initialiseSubTasks();


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

        return descriptor + " : " + activeSubTask.getTask().getDescriptor();
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
