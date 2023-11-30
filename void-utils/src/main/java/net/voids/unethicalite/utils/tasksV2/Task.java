package net.voids.unethicalite.utils.tasksV2;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;


//TODO:
// - we want to be able to pass custom validate/completion conditions to generic tasks
// - this means we should somehow accept



/**
 * Carry out some task within the game.
 */
@Slf4j
public abstract class Task
{
    /**
     * a simple string describing what the task is doing.
     */
    @Getter
    protected final String descriptor;

    private final TaskConfig taskConfig = new TaskConfig();

    @Getter
    private boolean initialisedAtleastOnce = false;

    @Getter
    private boolean initialised = false;

    @Getter
    private Failure failure; //in the event a failure occurs, store it here.

    @Setter
    @Getter
    protected int failCount = 0;

    @Getter
    @Setter
    protected int maxFailCount = 0;


    public Task(String descriptor)
    {
        this.descriptor = descriptor;
    }

    public TaskConfig config()
    {
        return this.taskConfig;
    }


    /**
     *
     * @return true if the task was stopped, and was previously running.
     */
    public boolean halt()
    {
        if (!this.initialised)
        {
            return false;
        }

        onHalt();
        return true;
    }

    /**
     *
     * @return true if the task is running, and was previously stopped.
     */
    protected boolean initialise()
    {
        //TODO:
        // - add some validation code here that ensures that next task does not have some invalid configuration.

        if (this.initialised)
        {
            return false;
        }

        this.initialised = true;
        this.initialisedAtleastOnce = true;
        onInitialise();
        return true;
    }


    /**
     * loops the plugin. will execute code and initialise if necessary.
     */
    public final int loop()
    {
        if (!this.initialised)
        {
            initialise();
        }

        if (this.isFailed())
        {
            //task failed on initialisation.
            return 0;
        }

        return this.execute();
    }


    /**
     *
     * @param type - specify the failure type
     * @param message - optional message to display describing failure.
     * @param resolveToTask - the task that should be run to attempt to resolve the failure.
     */
    public void failed(Failure.Type type, String message, Task resolveToTask)
    {
        this.failure = new Failure(type, message, resolveToTask);
    }

    /**
     *
     * @return true if the task has failed.
     */
    public boolean isFailed()
    {
        return failure != null;
    }


    /**
     *
     * @return true if the generic completion conditions are met, as well as any bespoke conditions.
     */
    public boolean isCompleted()
    {
        return completionCondition();
    }


    /**
     *
     * @return true if generic completion conditions are met.
     */
    public abstract boolean completionCondition();


    /**
     * Contains the actual script content that provides in-game functionality to the task.
     */
    abstract protected int execute();


    /**
     * execute some code when the task is initialised.
     */
    protected abstract void onInitialise();

    /**
     * execute some code when the task is halted.
     */
    protected void onHalt()
    {
        return;
    }


    /**
     *
     * @return the logger the task should use.
     */
    protected Logger getLogger()
    {
        return log;
    }
}
