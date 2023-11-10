package net.voids.unethicalite.cowkill.tasks;

import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.NPC;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.movement.Reachable;
import net.voids.unethicalite.cowkill.data.Areas;
import net.voids.unethicalite.utils.api.Activity;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

import java.util.ArrayList;
import java.util.Optional;

public class AttackCow extends Task
{
    public AttackCow(Job job)
    {
        super(job);

        interruptableBy = new ArrayList<>()
        {
            {
                add(Eat.class);
                add(Withdraw.class);
            }
        };

    }

    private NPC activeCow;

    @Override
    public String getStatus()
    {
        return "attacking cow.";
    }

    @Override
    public Activity getActivity()
    {
        return Activity.ATTACKING;
    }

    @Override
    public boolean validate()
    {
        return Equipment.fromSlot(EquipmentInventorySlot.WEAPON).getName().endsWith(" sword")
                && Combat.getCurrentHealth() >= 3;
    }

    @Override
    public void execute()
    {
        if (Players.getLocal() != null && !Areas.FALADOR_COW_FIELD.contains(Players.getLocal().getWorldLocation()))
        {
            if (!VoidMovement.walkToPolygonArea(Areas.FALADOR_COW_FIELD, 100))
            {
                //timed out while travelling to cow field.
                return;
            }
        }

        if (activeCow == null || activeCow.isDead() || (activeCow.isInteracting() && !activeCow.getInteracting().equals(Players.getLocal())))
        {
            //active cow was invalid so find a new one.

            Optional < NPC > cow = Optional.ofNullable(
                    NPCs.getNearest(npc ->
                            npc.getName().equals("Cow")
                                    && !npc.isInteracting()
                                    && !npc.isDead()
                                    && Areas.FALADOR_COW_FIELD.contains(npc.getWorldLocation())
                                    && Reachable.isInteractable(npc)
                    )
            );

            if (cow.isEmpty())
            {
                //could not locate cow to attack.
                return;
            }

            activeCow = cow.get();
        }

        if (!activeCow.isInteracting())
        {
            activeCow.interact("Attack");
        }

        if (!Time.sleepTicksUntil(() -> activeCow.isInteracting() || activeCow.isDead(), 10))
        {
            //failed to interact with cow
            return;
        }

        if (activeCow.isDead() ||
                (activeCow.isInteracting()
                        && (Players.getLocal() == null
                        || !Players.getLocal().isInteracting()
                        || !activeCow.getInteracting().equals(Players.getLocal())
                )
        ))
        {
           //cow is invalid to attack after selection
            return;
        }

        Time.sleepTicksUntil(() -> activeCow.isDead(), 120);
        Time.sleepTicks(5);
    }
}
