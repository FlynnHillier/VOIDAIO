package net.voids.unethicalite.command;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.voids.unethicalite.utils.events.TaskChangeEvent;

import javax.inject.Singleton;


@Slf4j
@Singleton
public class State
{
    private static State state;

    public static State getInstance()
    {
        if (state == null)
        {
            state = new State();
        }

        return state;
    }


    SocketCommunication socket = SocketCommunication.getInstance();

    @Getter
    @Setter
    private String managerTitle;

    @Getter
    private String jobTitle = "";

    @Getter
    private String taskStatus;


    public void onTaskChange(TaskChangeEvent event)
    {
        if (event.getTask() != null)
        {
            if (jobTitle == null || !event.getTask().getJob().getTitle().equals(jobTitle))
            {
                //job title has changed.
                setJobTitle(event.getTask().getJob().getTitle());
            }

            if (taskStatus == null || !event.getTask().getStatus().equals(taskStatus))
            {
                //task status has changed.
                setTaskStatus(event.getTask().getStatus());
            }

        }
    }

    public void setJobTitle(String jobTitle)
    {
        this.jobTitle = jobTitle;
        socket.reportJobChanged();
    }

    public void setTaskStatus(String taskStatus)
    {
        this.taskStatus = taskStatus;
        socket.reportTaskChanged();
    }

}
