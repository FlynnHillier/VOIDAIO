package net.voids.unethicalite.utils.api;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import net.unethicalite.client.Static;


@Slf4j
public class VoidMovement
{

    private static final int DEFAULT_WALKTO_TIMEOUT = 100; //the default tick timeout for a walkTo event.
    private static final int DESTINATION_DISTANCE = 8; //distance to get inside before ceasing execution.

    public static boolean interrupted;

    public static void walk(WorldPoint destination, final int rand)
    {
        walk(destination, rand, rand);
    }

    public static void walk(WorldPoint destination, final int x, final int y)
    {
        destination = destination
                .dx(Rand.nextInt(-x, x + 1))
                .dy(Rand.nextInt(-y, y + 1));

        Movement.walk(destination);
    }

    public static void walkTo(WorldPoint destination)
    {
        walkTo(destination, 0);
    }

    public static void walkTo(WorldPoint destination, int radius)
    {
        walkTo(destination, radius, DEFAULT_WALKTO_TIMEOUT);
    }

    public static void walkTo(WorldPoint destination, int radius, int tickTimeout)
    {
        walkTo(destination, radius, tickTimeout, DESTINATION_DISTANCE);
    }

    public static void walkTo(
            WorldPoint destination,
            int radius,
            int tickTimeout,
            int destinationDistance)
    {
        int start = Static.getClient().getTickCount();

        if (radius > 0)
        {
            destination =
                    destination.dx(Rand.nextInt(-radius, radius + 1)).dy(Rand.nextInt(-radius, radius + 1));
        }

        do
        {
            if (!Movement.isWalking() && Static.getClient().getGameState() != GameState.LOADING)
            {
                Movement.walkTo(destination);

                if (!Players.getLocal().isMoving())
                {
                    Time.sleepTick();
                }
            }

            Time.sleepTick();
        } while (!interrupted
                && Players.getLocal().distanceTo(destination) > destinationDistance
                && Static.getClient().getTickCount() <= start + tickTimeout
                && (Static.getClient().getGameState() == GameState.LOADING
                || Static.getClient().getGameState() == GameState.LOGGED_IN));

        interrupted = false;
    }


    public static void walkToArea(
            WorldArea worldArea
    )
    {
        walkToArea(worldArea, DEFAULT_WALKTO_TIMEOUT);
    }

    public static void walkToArea(
            WorldArea worldArea,
            int tickTimeout)
    {
        int start = Static.getClient().getTickCount();

        do
        {
            if (!Movement.isWalking() && Static.getClient().getGameState() != GameState.LOADING)
            {
                Movement.walkTo(worldArea);

                if (!Players.getLocal().isMoving())
                {
                    Time.sleepTick();
                }
            }

            Time.sleepTick();
        } while (!interrupted
                && !worldArea.contains(Players.getLocal().getWorldLocation())
                && Static.getClient().getTickCount() <= start + tickTimeout
                && (Static.getClient().getGameState() == GameState.LOADING
                || Static.getClient().getGameState() == GameState.LOGGED_IN));


        interrupted = false;
    }



    public static void walkToBank(BankLocation bankLocation)
    {
        BankLocation nearest = BankLocation.getNearest();
        WorldPoint worldPoint;

        switch (nearest)
        {
            case LUMBRIDGE_BANK:
                worldPoint =  new WorldArea(3208, 3220, 3, 1, 2).getRandom();
                break;
            default:
                worldPoint = BankLocation.getNearest().getArea().getCenter();
        }

        walkTo(worldPoint, 0, 100, 3);
    }

    public static boolean walkToNearestBank()
    {
        if (BankLocation.getNearest() == null)
        {
            return false;
        }

        walkToBank(BankLocation.getNearest());
        return true;
    }

}
