package net.voids.unethicalite.utils.api;

import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.coords.PolygonalArea;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import net.unethicalite.client.Static;

import java.util.ArrayList;


@Slf4j
public class VoidMovement
{

    private static final int DEFAULT_WALKTO_TIMEOUT = 100; //the default tick timeout for a walkTo event.
    private static final int DESTINATION_DISTANCE = 3; //distance to get inside before ceasing execution.

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

        WorldPoint mutatedDestination = destination;
        if (radius > 0)
        {
            mutatedDestination =
                    destination.dx(Rand.nextInt(-radius, radius + 1)).dy(Rand.nextInt(-radius, radius + 1));
        }

        do
        {
            walkTowardsPoint(mutatedDestination);

            Time.sleepTick();
        } while (!interrupted
                && Static.getClient().getTickCount() <= start + tickTimeout
                && Players.getLocal().distanceTo(mutatedDestination) > destinationDistance
                && (Static.getClient().getGameState() == GameState.LOADING
                || Static.getClient().getGameState() == GameState.LOGGED_IN)

        );

        interrupted = false;
    }


    public static boolean walkToArea(
            WorldArea worldArea
    )
    {
        return walkToArea(worldArea, DEFAULT_WALKTO_TIMEOUT);
    }

    public static boolean walkToArea(
            WorldArea worldArea,
            int tickTimeout)
    {
        int start = Static.getClient().getTickCount();

        do
        {
            walkTowardsArea(worldArea);

            Time.sleepTick();
        } while (!interrupted
                && !worldArea.contains(Players.getLocal().getWorldLocation())
                && Static.getClient().getTickCount() <= start + tickTimeout
                && (Static.getClient().getGameState() == GameState.LOADING
                || Static.getClient().getGameState() == GameState.LOGGED_IN));


        final boolean wasInterrupted = interrupted;
        interrupted = false;
        return !wasInterrupted && Static.getClient().getTickCount() <= start + tickTimeout;

    }



    public static boolean walkToPolygonArea(
            PolygonalArea polygonArea)
    {
        return walkToPolygonArea(polygonArea, DEFAULT_WALKTO_TIMEOUT);
    }

    public static boolean walkToPolygonArea(
            PolygonalArea polygonArea,
            int tickTimeout)
    {
        int start = Static.getClient().getTickCount();

        do
        {
            walkTowardsPoint(polygonArea.getRandomTile());

            Time.sleepTick();
        } while (!interrupted
                && !polygonArea.contains(Players.getLocal().getWorldLocation())
                && Static.getClient().getTickCount() <= start + tickTimeout
                && (Static.getClient().getGameState() == GameState.LOADING
                || Static.getClient().getGameState() == GameState.LOGGED_IN));

        interrupted = false;

        if (Static.getClient().getTickCount() >= start + tickTimeout)
        {
            return false;
        }
        return true;
    }


    public static boolean walkToNPC(NPC npc)
    {
        return walkToNPC(npc, DESTINATION_DISTANCE);
    }

    public static boolean walkToNPC(NPC npc, int stopAtDistanceFromNpc)
    {
        return walkToNPC(npc, stopAtDistanceFromNpc, DEFAULT_WALKTO_TIMEOUT);
    }



    public static boolean walkToNPC(NPC npc, int stopAtDistanceFromNpc, int tickTimeout)
    {
        int start = Static.getClient().getTickCount();

        do
        {
            walkTowardsPoint(npc.getWorldLocation());
            Time.sleepTick();
        } while (!interrupted
                && Static.getClient().getTickCount() <= start + tickTimeout
                && (
                    Players.getLocal().getWorldLocation().distanceTo(npc.getWorldLocation()) > stopAtDistanceFromNpc
                    || !Reachable.isWalkable(npc.getWorldLocation())
            )
        );

        interrupted = false;

        return Static.getClient().getTickCount() <= start + tickTimeout;
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

    public static ArrayList<WorldPoint> getNonBlockedAdjacentTiles(WorldPoint worldPoint)
    {
        ArrayList<Pair<Integer, Integer>> vectors = new ArrayList<>();

        vectors.add(new Pair<>(0, 1));
        vectors.add(new Pair<>(1, 0));
        vectors.add(new Pair<>(0, -1));
        vectors.add(new Pair<>(-1, 0));


        ArrayList<WorldPoint> nonBlockedAdjacent = new ArrayList<WorldPoint>();
        for (Pair<Integer, Integer> vector : vectors)
        {
            WorldPoint check = new WorldPoint(worldPoint.getX() + vector.component1(), worldPoint.getY() + vector.component2(), worldPoint.getPlane());

            if (!Reachable.isWalled(worldPoint, check) && !Reachable.isObstacle(check))
            {
                nonBlockedAdjacent.add(check);
            }
        }


        return nonBlockedAdjacent;
    }


    public static ArrayList<WorldPoint> getAreaOuterTiles(WorldArea area)
    {
        ArrayList<Integer> Xs = new ArrayList<>();
        for (int i = 0; i < area.getWidth(); ++i)
        {
            Xs.add(area.getX() + i);
        }

        ArrayList<Integer> Ys = new ArrayList<>();
        for (int i = 0; i < area.getHeight(); ++i)
        {
            Ys.add(area.getY() + i);
        }

        ArrayList<WorldPoint> coordinates = new ArrayList<>();
        for (Integer x : Xs)
        {
            for (Integer y : Ys)
            {
                coordinates.add(new WorldPoint(x, y, area.getPlane()));
            }
        }

        return coordinates;
    }







    private static void walkTowardsArea(WorldArea worldArea)
    {
        if (!Movement.isWalking() && Static.getClient().getGameState() != GameState.LOADING)
        {
            Movement.walkTo(worldArea);

            if (!Players.getLocal().isMoving())
            {
                Time.sleepTick();
            }
        }
    }

    private static void walkTowardsPoint(WorldPoint worldPoint)
    {
        if (!Movement.isWalking() && Static.getClient().getGameState() != GameState.LOADING)
        {
            Movement.walkTo(worldPoint);

            if (!Players.getLocal().isMoving())
            {
                Time.sleepTick();
            }
        }
    }
}
