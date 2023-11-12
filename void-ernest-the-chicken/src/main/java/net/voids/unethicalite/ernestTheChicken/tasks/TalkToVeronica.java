package net.voids.unethicalite.ernestTheChicken.tasks;

import net.runelite.api.NPC;
import net.runelite.api.NpcID;
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

public class TalkToVeronica extends Task
{
    public TalkToVeronica(Job job)
    {
        super(job);
    }

    @Override
    public String getStatus()
    {
        return "talking to veronica";
    }

    @Override
    public boolean validate()
    {
        //The quest has not yet been started.
        return Static.getClient().getVarpValue(32) == 0;
    }

    @Override
    public void execute()
    {
        if (!Areas.DRAYNOR_MANOR_GATE.contains(Players.getLocal().getWorldLocation()))
        {
            VoidMovement.walkToArea(Areas.DRAYNOR_MANOR_GATE, 300);
        }

        NPC veronicaNPC = NPCs.getNearest(NpcID.VERONICA);

        if (veronicaNPC == null)
        {
            job.getLogger().info("failed to locate veronica NPC.");
            return;
        }

        veronicaNPC.interact("Talk-to");

        if (!Time.sleepTicksUntil(Dialog::isOpen, 20))
        {
            job.getLogger().info("failed to open dialogue window with veronica");
            return;
        }

        VoidDialogue.completeDialogue("Yes.");
    }
}
