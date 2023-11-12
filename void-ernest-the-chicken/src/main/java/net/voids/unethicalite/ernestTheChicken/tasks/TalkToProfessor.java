package net.voids.unethicalite.ernestTheChicken.tasks;

import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.client.Static;
import net.voids.unethicalite.ernestTheChicken.data.Areas;
import net.voids.unethicalite.utils.api.VoidDialogue;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

public class TalkToProfessor extends Task
{
    public TalkToProfessor(Job job)
    {
        super(job);
    }

    @Override
    public String getStatus()
    {
        return "talking to proffessor.";
    }


    @Override
    public boolean validate()
    {
        return Static.getClient().getVarpValue(32) == 1;
    }

    @Override
    public void execute()
    {
        if (!Areas.DRAYNOR_MANOR_TOP_FLOOR.contains(Players.getLocal().getWorldLocation()))
        {
            VoidMovement.walkTo(new WorldPoint(3105, 3364, 2), 0, 200, 0);
        }

        NPC professor = NPCs.getNearest(NpcID.PROFESSOR_ODDENSTEIN);

        if (professor == null)
        {
            job.getLogger().info("unable to locate professor.");
            return;
        }

        if (!VoidMovement.walkToNPC(professor))
        {
            job.getLogger().info("failed to reach professor");
            return;
        }

        professor.interact("Talk-to");

        if (!Time.sleepTicksUntil(Dialog::isOpen, 20))
        {
            job.getLogger().info("failed to talk to professor");
            return;
        }

        if (!VoidDialogue.completeDialogue(
                "I'm looking for a guy called Ernest.",
                "Change him back this instant!"
                )
        )
        {
            job.getLogger().info("failed to complete dialogue with professor.");
            return;
        }
    }


}
