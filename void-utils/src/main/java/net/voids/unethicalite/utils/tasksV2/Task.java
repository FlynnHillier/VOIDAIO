package net.voids.unethicalite.utils.tasksV2;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.BooleanSupplier;


//TODO:
// - we want to be able to pass custom validate/completion conditions to generic tasks
// - this means we should somehow accept



/**
 * Carry out some task within the game.
 */
@Slf4j
public abstract class Task
{

    private boolean initialised;

    @Getter
    private Failure failure; //in the event a failure occurs, store it here.

    protected boolean failable = false; //specify wether it is possible for the task to fail.

    @Setter
    @Getter
    protected int failCount = 0;

    @Getter
    @Setter
    protected int maxFailCount = 0;


    private BooleanSupplier bespokeValidationCondition;

    private BooleanSupplier bespokeCompletionCondition;


    public Task(String descriptor)
    {
        this.descriptor = descriptor;
    }


    public Task withValidationCondition(BooleanSupplier booleanSupplier)
    {
        this.bespokeValidationCondition = booleanSupplier;

        return this;
    }

    public Task withCompletionCondition(BooleanSupplier booleanSupplier)
    {
        this.bespokeCompletionCondition = booleanSupplier;

        return this;
    }




    /**
     * a simple string describing what the task is doing.
     */
    @Getter
    final String descriptor;

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
        if (this.initialised)
        {
            return false;
        }

        this.initialised = true;
        onInitialise();
        return true;
    }


    /**
     * loops the plugin. will execute code and initialise if necessary.
     */
    public final void loop()
    {
        if (!this.initialised)
        {
            initialise();
        }

        if (!this.isFailed())
        {
            //likely task failed on initialisation.
            this.execute();
        }

        //TODO:
        // - add functionality here such that tasks can self handle themselves?
        // - e.g task does not rely on complex task to handle failure.
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
        return failable && failure != null;
    }


    /**
     *
     * @return true if all validation conditions are met (both generic and dynamic)
     */
    public boolean isValidated()
    {
        return validationCondition()
                && (bespokeValidationCondition == null
                || bespokeValidationCondition.getAsBoolean());
    }

    public boolean isCompleted()
    {
        return completionCondition()
                && (bespokeCompletionCondition == null
                || bespokeCompletionCondition.getAsBoolean());
    }




    /**
     *
     * @return true if generic conditions are specified;
     */
    abstract protected boolean validationCondition();


    /**
     * Note: this method should never return true if validate also equates to true at the same time.
     * The point of the validate method is to insinuate that the task needs to be carried out.
     * Therefore, if this is simultaneously considered 'complete' this is pointless.
     * @return true if the task has been completed.
     */
    public abstract boolean completionCondition();


    /**
     * Contains the actual script content that provides in-game functionality to the task.
     */
    abstract protected void execute();


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
     * @param failure - the failure that should be attempted to handle.
     * @return true if some solution was implemented.
     */
    protected Optional<Task> handleFailure(Failure failure)
    {
        return Optional.empty();
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
